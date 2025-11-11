// ===================================
// 🔧 CONFIGURACIÓN DE API (solo loan-service)
// ===================================
const LOAN_API_URL = "http://localhost:8082/api/loans";

// ===================================
// 📌 ELEMENTOS DEL DOM
// ===================================
const loanForm = document.getElementById("loanForm");
const loanBody = document.getElementById("loanBody");
const saveBtn = document.getElementById("saveBtn");
const classroomSelect = document.getElementById("classroomCode");
const statusFilter = document.getElementById("statusFilter");
let editingId = null;

// ===================================
// 🚀 INICIALIZACIÓN
// ===================================
document.addEventListener("DOMContentLoaded", () => {
  setMinDate();
  loadClassrooms(); // usa /api/loans/classrooms (expuesto por tu controller)
  loadLoans();
});

// ===================================
// 📅 Establecer fecha mínima (hoy)
// ===================================
function setMinDate() {
  const today = new Date().toISOString().split('T')[0];
  const loanDateEl = document.getElementById("loanDate");
  if (loanDateEl) loanDateEl.setAttribute('min', today);
}

// ===================================
// 🏫 Cargar aulas desde endpoint expuesto por loan-service: /api/loans/classrooms
// ===================================
async function loadClassrooms() {
  if (!classroomSelect) return;
  classroomSelect.innerHTML = '<option value="">Cargando aulas...</option>';

  try {
    const res = await fetch(`${LOAN_API_URL}/classrooms`);
    if (!res.ok) throw new Error(`HTTP ${res.status}`);
    const data = await res.json();

    classroomSelect.innerHTML = '<option value="">Selecciona un aula...</option>';

    if (!Array.isArray(data) || data.length === 0) {
      classroomSelect.innerHTML += '<option value="" disabled>No hay aulas disponibles</option>';
      return;
    }

    data.forEach(c => {
      // Se asume que el classroom tiene campos: id, name, location, capacity, state
      const opt = document.createElement('option');
      opt.value = c.id ?? c.code ?? '';
      opt.textContent = `${c.name ?? c.id} - ${c.location ?? ''} (${c.capacity ?? 'N/A'})${c.state ? ' - ' + c.state : ''}`;
      if (c.state && c.state !== 'AVAILABLE') opt.disabled = true;
      classroomSelect.appendChild(opt);
    });
  } catch (err) {
    console.error("Error cargando aulas:", err);
    classroomSelect.innerHTML = '<option value="">Error cargando aulas</option>';
  }
}

// ===================================
// 📄 Cargar todos los préstamos (GET /api/loans)
// ===================================
async function loadLoans() {
  try {
    showTableLoading();
    const res = await fetch(LOAN_API_URL);
    if (!res.ok) throw new Error(`HTTP ${res.status}`);
    const loans = await res.json();
    renderLoansTable(loans);
  } catch (err) {
    console.error("Error loading loans:", err);
    loanBody.innerHTML = '<tr><td colspan="9" class="error">Error loading loans</td></tr>';
  }
}

function showTableLoading() {
  loanBody.innerHTML = '<tr><td colspan="9" class="loading">Cargando préstamos...</td></tr>';
}

function renderLoansTable(loans) {
  loanBody.innerHTML = "";
  if (!Array.isArray(loans) || loans.length === 0) {
    loanBody.innerHTML = '<tr><td colspan="9" class="no-data">No se encontraron préstamos</td></tr>';
    return;
  }

  loans.forEach(loan => {
    const tr = document.createElement("tr");
    const statusClass = loan.status ? loan.status.toLowerCase() : 'unknown';
    tr.className = `status-${statusClass}`;

    tr.innerHTML = `
      <td>${loan.id ?? ''}</td>
      <td>${loan.nameResponsible ?? ''}</td>
      <td>${loan.userType ?? ''}</td>
      <td>${loan.academicProgram ?? ''}</td>
      <td>${loan.classroomCode ?? ''}</td>
      <td>${formatDate(loan.loanDate)}</td>
      <td>${loan.startTime ?? ''}</td>
      <td>${loan.endTime ?? ''}</td>
      <td class="purpose-cell">${escapeHtml(loan.purpose ?? '')}</td>
      <td><span class="status-badge ${statusClass}">${loan.status ?? ''}</span></td>
      <td class="action-buttons">
        <button class="btn-edit" onclick="editLoan(${loan.id})" title="Editar">✏️</button>
        <button class="btn-delete" onclick="deleteLoan(${loan.id})" title="Eliminar">🗑️</button>
        ${getStatusActions(loan)}
      </td>
    `;
    loanBody.appendChild(tr);
  });
}

// Evitar inyección simple en propósito
function escapeHtml(str) {
  return str.replaceAll('&','&amp;').replaceAll('<','&lt;').replaceAll('>','&gt;');
}

// ===================================
// 🎯 Botones por estado (usa endpoints específicos)
 // - activate -> PATCH /{id}/activate
 // - cancel   -> PATCH /{id}/cancel
// ===================================
function getStatusActions(loan) {
  let buttons = '';
  if (loan.status === 'RESERVED') {
    buttons += `<button class="btn-activate" onclick="activateLoan(${loan.id})" title="Activar">✔️ Activate</button>`;
  }
  if (loan.status !== 'CANCELLED') {
    buttons += `<button class="btn-cancel" onclick="cancelLoan(${loan.id})" title="Cancelar">❌ Cancel</button>`;
  }
  return buttons;
}

// ===================================
// 📝 Crear o actualizar préstamo (POST / PUT)
// ===================================
loanForm.addEventListener("submit", async (e) => {
  e.preventDefault();

  const loanData = {
    nameResponsible: document.getElementById("nameResponsible").value.trim(),
    userType: document.getElementById("userType").value,
    academicProgram: document.getElementById("academicProgram").value,
    classroomCode: parseInt(document.getElementById("classroomCode").value) || null,
    loanDate: document.getElementById("loanDate").value,
    startTime: document.getElementById("startTime").value,
    endTime: document.getElementById("endTime").value,
    purpose: document.getElementById("purpose").value.trim(),
    status: document.getElementById("status").value
  };

  // Validaciones básicas
  if (!loanData.nameResponsible) return alert("Ingresa el nombre del responsable");
  if (!loanData.userType) return alert("Selecciona el tipo de usuario");
  if (!loanData.academicProgram) return alert("Selecciona el programa académico");
  if (!loanData.classroomCode) return alert("Selecciona un aula");
  if (!loanData.loanDate) return alert("Selecciona una fecha");
  if (!loanData.startTime || !loanData.endTime) return alert("Selecciona hora inicio y fin");
  if (loanData.startTime >= loanData.endTime) return alert("La hora de fin debe ser posterior a la hora de inicio");

  try {
    let res;
    if (editingId) {
      res = await fetch(`${LOAN_API_URL}/${editingId}`, {
        method: "PUT",
        headers: { "Content-Type": "application/json" },
        body: JSON.stringify(loanData)
      });
    } else {
      res = await fetch(LOAN_API_URL, {
        method: "POST",
        headers: { "Content-Type": "application/json" },
        body: JSON.stringify(loanData)
      });
    }

    if (!res.ok) {
      // intentar leer cuerpo con mensaje de error
      let errText = `HTTP ${res.status}`;
      try {
        const json = await res.json();
        errText = json.message || JSON.stringify(json);
      } catch (_) {
        const text = await res.text().catch(() => null);
        if (text) errText = text;
      }
      throw new Error(errText);
    }

    const saved = await res.json();
    alert(editingId ? "✅ Préstamo actualizado" : "✅ Préstamo creado");
    resetForm();
    loadLoans();
  } catch (err) {
    console.error("Error saving loan:", err);
    alert("Error saving loan: " + err.message);
  }
});

// ===================================
// ✏️ Editar préstamo (GET /{id})
// ===================================
async function editLoan(id) {
  try {
    const res = await fetch(`${LOAN_API_URL}/${id}`);
    if (!res.ok) throw new Error(`HTTP ${res.status}`);
    const loan = await res.json();

    document.getElementById("nameResponsible").value = loan.nameResponsible ?? '';
    document.getElementById("userType").value = loan.userType ?? '';
    document.getElementById("academicProgram").value = loan.academicProgram ?? '';
    document.getElementById("classroomCode").value = loan.classroomCode ?? '';
    document.getElementById("loanDate").value = loan.loanDate ?? '';
    document.getElementById("startTime").value = loan.startTime ?? '';
    document.getElementById("endTime").value = loan.endTime ?? '';
    document.getElementById("purpose").value = loan.purpose ?? '';
    document.getElementById("status").value = loan.status ?? 'RESERVED';

    editingId = id;
    saveBtn.textContent = "Actualizar préstamo";
    loanForm.scrollIntoView({ behavior: "smooth" });
  } catch (err) {
    console.error("Error loading loan:", err);
    alert("Error loading loan data");
  }
}

// ===================================
// 🗑️ Eliminar préstamo (DELETE /{id})
// ===================================
async function deleteLoan(id) {
  if (!confirm("¿Seguro que deseas eliminar este préstamo?")) return;
  try {
    const res = await fetch(`${LOAN_API_URL}/${id}`, { method: "DELETE" });
    if (res.status === 204 || res.ok) {
      alert("✅ Préstamo eliminado");
      loadLoans();
    } else {
      let txt = await res.text().catch(()=>null);
      throw new Error(txt || `HTTP ${res.status}`);
    }
  } catch (err) {
    console.error("Error deleting loan:", err);
    alert("Error deleting loan: " + err.message);
  }
}

// ===================================
// ✅ Activar préstamo (PATCH /{id}/activate)
// ===================================
async function activateLoan(id) {
  try {
    const res = await fetch(`${LOAN_API_URL}/${id}/activate`, { method: "PATCH" });
    if (!res.ok) {
      const json = await res.json().catch(()=>null);
      throw new Error((json && json.message) || `HTTP ${res.status}`);
    }
    alert("✅ Préstamo activado");
    loadLoans();
  } catch (err) {
    console.error("Error activating loan:", err);
    alert("Error: " + err.message);
  }
}

// ===================================
// ❌ Cancelar préstamo (PATCH /{id}/cancel)
// ===================================
async function cancelLoan(id) {
  if (!confirm("¿Seguro que deseas cancelar este préstamo?")) return;
  try {
    const res = await fetch(`${LOAN_API_URL}/${id}/cancel`, { method: "PATCH" });
    if (!res.ok) {
      const json = await res.json().catch(()=>null);
      throw new Error((json && json.message) || `HTTP ${res.status}`);
    }
    alert("✅ Préstamo cancelado");
    loadLoans();
  } catch (err) {
    console.error("Error cancelling loan:", err);
    alert("Error: " + err.message);
  }
}

// ===================================
// 🔄 Reset de formulario
// ===================================
function resetForm() {
  loanForm.reset();
  editingId = null;
  saveBtn.textContent = "Agregar Préstamo";
  setMinDate();
}

// ===================================
// 🔍 Filtrar por estado (usa GET /status/{status})
// ===================================
async function filterByStatus() {
  const status = (statusFilter && statusFilter.value) || '';
  try {
    showTableLoading();
    const url = status ? `${LOAN_API_URL}/status/${status}` : LOAN_API_URL;
    const res = await fetch(url);
    if (!res.ok) throw new Error(`HTTP ${res.status}`);
    const loans = await res.json();
    renderLoansTable(loans);
  } catch (err) {
    console.error("Error filtering loans:", err);
    loanBody.innerHTML = '<tr><td colspan="9" class="error">Error filtering loans</td></tr>';
  }
}

// ===================================
// ✨ Utilidades
// ===================================
function formatDate(dateString) {
  if (!dateString) return '';
  // Asumimos formato YYYY-MM-DD del backend
  const d = new Date(dateString + 'T00:00:00');
  if (isNaN(d)) return dateString;
  return d.toLocaleDateString('es-ES', { year: 'numeric', month: '2-digit', day: '2-digit' });
}

// ===================================
// 🌐 Función opcional para cambiar estado arbitrario (PATCH /{id}/status?status=...)
// si en algún momento quieres usarla
// ===================================
async function changeLoanStatus(id, newStatus) {
  try {
    const res = await fetch(`${LOAN_API_URL}/${id}/status?status=${encodeURIComponent(newStatus)}`, {
      method: "PATCH"
    });
    if (!res.ok) throw new Error(`HTTP ${res.status}`);
    return await res.json();
  } catch (err) {
    console.error("Error changing status:", err);
    throw err;
  }
}

// Exportar funciones globales que se usan desde HTML inline (si aplica)
window.editLoan = editLoan;
window.deleteLoan = deleteLoan;
window.activateLoan = activateLoan;
window.cancelLoan = cancelLoan;
window.filterByStatus = filterByStatus;
