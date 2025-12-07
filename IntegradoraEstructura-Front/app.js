const API_PACIENTES = "http://localhost:8080/api/integradora/pacientes";
const API_CITAS = "http://localhost:8080/api/integradora/citas";
const API_HISTORIAL = "http://localhost:8080/api/integradora/historial";

// ====== NUEVO: ID del paciente que se está editando (null = modo crear) ======
let pacienteEditandoId = null;

// ================= PACIENTES =================

document
  .getElementById("formPaciente")
  ?.addEventListener("submit", function (e) {
    e.preventDefault();

    const paciente = {
      nombre: nombre.value,
      apellido: apellido.value,
      cedula: cedula.value,
      fechaNacimiento: fechaNacimiento.value,
      telefono: telefono.value,
      email: email.value,
      direccion: direccion.value,
      tipoSangre: tipoSangre.value,
    };

    // Si hay un id en edición usamos PUT, si no, POST
    const url = pacienteEditandoId
      ? `${API_PACIENTES}/${pacienteEditandoId}`
      : API_PACIENTES;

    const method = pacienteEditandoId ? "PUT" : "POST";

    fetch(url, {
      method,
      headers: { "Content-Type": "application/json" },
      body: JSON.stringify(paciente),
    })
      .then((r) => r.json())
      .then((data) => {
        alert(
          data.mensaje ||
            (pacienteEditandoId
              ? "Paciente actualizado"
              : "Paciente registrado")
        );
        cargarPacientes();

        // Resetear modo edición
        pacienteEditandoId = null;
        const btn = document.getElementById("btnGuardarPaciente");
        if (btn) btn.textContent = "Registrar Paciente";

        e.target.reset();
      })
      .catch(() => alert("Error al guardar paciente"));
  });

function cargarPacientes() {
  fetch(API_PACIENTES)
    .then((r) => r.json())
    .then((data) => {
      const tbody = document.getElementById("tablaPacientes");
      if (!tbody) return;

      tbody.innerHTML = "";
      (data.listaPacientes || []).forEach((p) => {
        tbody.innerHTML += `
          <tr>
              <td>${p.id}</td>
              <td>${p.nombre} ${p.apellido}</td>
              <td>${p.cedula}</td>
              <td>${p.telefono || ""}</td>
              <td>
                  <button class="btn btn-sm btn-danger" onclick="eliminarPaciente(${
                    p.id
                  })">
                      Eliminar
                  </button>
                  <button class="btn btn-sm btn-secondary ms-1" onclick="cargarPacienteEnFormulario(${
                    p.id
                  })">
                      Editar
                  </button>
              </td>
          </tr>
        `;
      });
    });
}

function eliminarPaciente(id) {
  if (!confirm("¿Eliminar paciente?")) return;

  fetch(`${API_PACIENTES}/${id}`, { method: "DELETE" })
    .then((r) => r.json())
    .then((data) => {
      alert(data.mensaje || "Paciente eliminado");
      cargarPacientes();
    })
    .catch(() => alert("Error al eliminar paciente"));
}

// ====== NUEVO: Cargar paciente en el formulario para editar ======
function cargarPacienteEnFormulario(id) {
  fetch(`${API_PACIENTES}/${id}`)
    .then((r) => r.json())
    .then((data) => {
      const p = data.paciente || data;

      if (!p) {
        alert("No se pudo cargar el paciente");
        return;
      }

      pacienteEditandoId = p.id;

      nombre.value = p.nombre || "";
      apellido.value = p.apellido || "";
      cedula.value = p.cedula || "";
      fechaNacimiento.value = p.fechaNacimiento || "";
      telefono.value = p.telefono || "";
      email.value = p.email || "";
      direccion.value = p.direccion || "";
      tipoSangre.value = p.tipoSangre || "";

      const btn = document.getElementById("btnGuardarPaciente");
      if (btn) btn.textContent = "Actualizar Paciente";
    })
    .catch(() => alert("Error al cargar paciente"));
}

// ================= CITAS (COLA) =================

function agregarCita() {
  const cita = {
    pacienteId: parseInt(
      document.getElementById("citaPacienteId").value || "0"
    ),
    nombreMedico: document.getElementById("citaMedico").value,
    especialidad: document.getElementById("citaEspecialidad").value,
    fechaHora: document.getElementById("citaFecha").value,
    motivo: document.getElementById("citaMotivo").value,
    prioridad: document.getElementById("citaPrioridad").value,
    estado: "PENDIENTE",
  };

  fetch(`${API_CITAS}/agregar`, {
    method: "POST",
    headers: { "Content-Type": "application/json" },
    body: JSON.stringify(cita),
  })
    .then(async (r) => {
      if (!r.ok) {
        const txt = await r.text();
        throw new Error(txt || "Error al agregar cita");
      }
      return r.json();
    })
    .then((data) => {
      alert(data.mensaje || "Cita agregada a la cola");
      cargarCola();
    })
    .catch((err) => alert("Error: " + err.message));
}

function verSiguiente() {
  fetch(`${API_CITAS}/siguiente`)
    .then((r) => {
      if (r.status === 204) {
        alert("No hay citas en la cola");
        return null;
      }
      return r.json();
    })
    .then((cita) => {
      if (!cita) return;
      alert(
        `Siguiente cita:\n` +
          `Paciente ID: ${cita.pacienteId}\n` +
          `Médico: ${cita.nombreMedico}\n` +
          `Prioridad: ${cita.prioridad}\n` +
          `Estado: ${cita.estado}`
      );
    })
    .catch(() => alert("Error al obtener siguiente cita"));
}

function atenderSiguiente() {
  fetch(`${API_CITAS}/atender`, { method: "POST" })
    .then((r) => {
      if (r.status === 204) {
        alert("No hay citas en la cola");
        return null;
      }
      return r.json();
    })
    .then((cita) => {
      if (!cita) return;
      alert(
        `Atendiendo cita de paciente ID: ${cita.pacienteId}\n` +
          `Médico: ${cita.nombreMedico}\n` +
          `Prioridad: ${cita.prioridad}`
      );
      cargarCola();
    })
    .catch(() => alert("Error al atender cita"));
}

function cargarCola() {
  fetch(API_CITAS)
    .then((r) => r.json())
    .then((data) => {
      const tbody = document.getElementById("tablaCola");
      if (!tbody) return;

      tbody.innerHTML = "";
      (data || []).forEach((c) => {
        tbody.innerHTML += `
          <tr>
              <td>${c.pacienteId}</td>
              <td>${c.nombreMedico}</td>
              <td>${c.especialidad}</td>
              <td>${c.fechaHora || ""}</td>
              <td>${c.prioridad}</td>
              <td>${c.estado}</td>
          </tr>
        `;
      });
    })
    .catch(() => console.log("Error al cargar cola"));
}

// ================= HISTORIAL (PILA) =================

function agregarHistorial() {
  const registro = {
    pacienteId: document.getElementById("histPacienteId").value,
    nombreMedico: document.getElementById("histMedico").value,
    diagnostico: document.getElementById("histDiagnostico").value,
    tratamiento: document.getElementById("histTratamiento").value,
  };

  fetch(API_HISTORIAL, {
    method: "POST",
    headers: { "Content-Type": "application/json" },
    body: JSON.stringify(registro),
  })
    .then((r) => r.json())
    .then((data) => {
      alert(data.mensaje || "Registro agregado al historial");
    })
    .catch(() => alert("Error al agregar historial"));
}

function verHistorial() {
  const id = document.getElementById("histPacienteId").value;
  if (!id) {
    alert("Escribe el ID del paciente para ver su historial");
    return;
  }

  fetch(`${API_HISTORIAL}/${id}`)
    .then((r) => r.json())
    .then((data) => {
      const tbody = document.getElementById("tablaHistorial");
      if (!tbody) return;

      tbody.innerHTML = "";
      (data || []).forEach((rg) => {
        tbody.innerHTML += `
          <tr>
              <td>${rg.fecha || ""}</td>
              <td>${rg.nombreMedico || ""}</td>
              <td>${rg.diagnostico || ""}</td>
              <td>${rg.tratamiento || ""}</td>
          </tr>
        `;
      });
    })
    .catch(() => alert("Error al obtener historial"));
}

// ================= ESTADÍSTICAS =================

function estadisticasPacientes() {
  fetch(`${API_PACIENTES}/estadisticas`)
    .then((r) => r.json())
    .then((data) => {
      alert(
        `Pacientes totales: ${data.total}\n` +
          `Estructura: ${data.estructura}\n` +
          `Agregar: ${data.complejidadAgregar}, Buscar: ${data.complejidadBuscar}`
      );
    })
    .catch(() => alert("Error al obtener estadísticas de pacientes"));
}

function estadisticasCola() {
  fetch(`${API_CITAS}/estadisticas`)
    .then((r) => r.json())
    .then((data) => {
      alert(
        `Citas en cola: ${data.total}\n` +
          `Estructura: ${data.estructura}\n` +
          `Cola vacía: ${data.vacia ? "Sí" : "No"}`
      );
    })
    .catch(() => alert("Error al obtener estadísticas de cola"));
}

function estadisticasHistorial() {
  const id = document.getElementById("histPacienteId")?.value;
  if (!id) {
    alert("Escribe el ID del paciente en la pestaña Historial");
    return;
  }

  fetch(`${API_HISTORIAL}/${id}/estadisticas`)
    .then((r) => r.json())
    .then((data) => {
      let msg =
        `Registros en historial: ${data.totalRegistros}\n` +
        `Estructura: ${data.estructura}\n` +
        `Apilar: ${data.complejidadApilar}, Desapilar: ${data.complejidadDesapilar}`;

      if (data.ultimoRegistro) {
        msg +=
          `\n\nÚltimo registro:\n` +
          `Fecha: ${data.ultimoRegistro.fecha}\n` +
          `Médico: ${data.ultimoRegistro.medico}\n` +
          `Diagnóstico: ${data.ultimoRegistro.diagnostico}`;
      }
      alert(msg);
    })
    .catch(() => alert("Error al obtener estadísticas de historial"));
}

// ================= AUTO-CARGA =================

cargarPacientes();
cargarCola();
