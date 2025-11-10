// ===================================
// 🔧 CONFIGURACIÓN DE APIs
// ===================================
const LOAN_API_URL = "http://localhost:8082/api/loans";
const CLASSROOM_API_URL = "http://localhost:8081/api/classrooms";

// ===================================
// 📌 ELEMENTOS DEL DOM
// ===================================
const loanForm = document.getElementById("loanForm");
const loanBody = document.getElementById("loanBody");
const saveBtn = document.getElementById("saveBtn");
const classroomSelect = document.getElementById("classroomCode");
let editingId = null;

// ===================================
// 🚀 INICIALIZACIÓN
// ===================================
document.addEventListener("DOMContentLoaded", () => {
  loadClassrooms();
  loadLoans();
  setMinDate();
});

// ===================================
// � ESTABLECER FECHA MÍNIMA (HOY)
// ===================================
function setMinDate() {
  const today = new Date().toISOString().split('T')[0];
  document.getElementById("loanDate").setAttribute('min', today);
}

// ===================================
// 🏫 CARGAR AULAS DISPONIBLES DESDE CLASSROOM SERVICE
// ===================================
async function loadClassrooms() {
  try {
    console.log("🔄 Cargando aulas desde Classroom Service...");
    const response = await fetch(CLASSROOM_API_URL);
    
    if (!response.ok) {
      throw new Error(`HTTP error! status: ${response.status}`);
    }
    
    const classrooms = await response.json();
    console.log("✅ Aulas cargadas:", classrooms);
    
    // Limpiar y llenar el select
    classroomSelect.innerHTML = '<option value="">Selecciona un aula...</option>';
    
    classrooms.forEach(classroom => {
      const option = document.createElement("option");
      option.value = classroom.id;
      option.textContent = `${classroom.name} - ${classroom.location} (Capacity: ${classroom.capacity})`;
      
      // Deshabilitar aulas no disponibles
      if (classroom.state !== "AVAILABLE") {
        option.disabled = true;
        option.textContent += ` - ${classroom.state}`;
      }
      
      classroomSelect.appendChild(option);
    });
    
    console.log("✅ Select de aulas poblado correctamente");
  } catch (error) {
    console.error("❌ Error cargando aulas:", error);
    classroomSelect.innerHTML = '<option value="">Error cargando aulas</option>';
    alert("⚠️ No se pudieron cargar las aulas. Verifica que el Classroom Service esté funcionando.");
  }
}

// ===================================
// 🔍 FUNCIONES DE DIAGNÓSTICO
// ===================================
async function showDiagnostic() {
  const panel = document.getElementById('diagnosticPanel');
  const content = document.getElementById('diagnosticContent');
  
  panel.style.display = 'block';
  content.textContent = 'Running diagnostic tests...\n\n';
  
  let results = '';
  
  try {
    // Test 1: Verificar URLs configuradas
    results += '1. CONFIGURACIÓN DE URLs:\n';
    results += `   - Classroom Service: ${CLASSROOM_API_URL}\n`;
    results += `   - Loan Service: ${LOAN_API_URL}\n\n`;
    
    // Test 2: Verificar conectividad a Classroom Service
    results += '2. CONECTIVIDAD CLASSROOM SERVICE:\n';
    content.textContent = results;
    
    try {
      const startTime = Date.now();
      const response = await fetch(CLASSROOM_API_URL, {
        method: 'GET',
        headers: {
          'Accept': 'application/json',
          'Content-Type': 'application/json'
        }
      });
      const endTime = Date.now();
      
      results += `   ✅ Status: ${response.status} ${response.statusText}\n`;
      results += `   ✅ Response time: ${endTime - startTime}ms\n`;
      results += `   ✅ Headers: ${JSON.stringify(Object.fromEntries(response.headers.entries()), null, 2)}\n`;
      
      if (response.ok) {
        const data = await response.json();
        results += `   ✅ Data type: ${Array.isArray(data) ? 'Array' : typeof data}\n`;
        results += `   ✅ Data length: ${Array.isArray(data) ? data.length : 'N/A'}\n`;
        
        if (Array.isArray(data) && data.length > 0) {
          results += `   ✅ First item: ${JSON.stringify(data[0], null, 2)}\n`;
        } else {
          results += `   ⚠️ No data received or empty array\n`;
        }
      } else {
        const errorText = await response.text();
        results += `   ❌ Error response: ${errorText}\n`;
      }
    } catch (error) {
      results += `   ❌ Connection failed: ${error.message}\n`;
      results += `   ❌ Error type: ${error.name}\n`;
    }
    
    results += '\n';
    
    // Test 3: Verificar DOM elements
    results += '3. ELEMENTOS DOM:\n';
    const elements = {
      'classroomSelect': document.getElementById('classroomCode'),
      'loanForm': document.getElementById('loanForm'),
      'reloadBtn': document.getElementById('reloadClassroomsBtn')
    };
    
    for (const [name, element] of Object.entries(elements)) {
      if (element) {
        results += `   ✅ ${name}: Found\n`;
        if (name === 'classroomSelect') {
          results += `      - Options count: ${element.options.length}\n`;
          results += `      - Current value: "${element.value}"\n`;
        }
      } else {
        results += `   ❌ ${name}: NOT FOUND\n`;
      }
    }
    
    results += '\n';
    
    // Test 4: Verificar si Docker está corriendo
    results += '4. RECOMENDACIONES:\n';
    results += '   • Verificar que Docker containers están corriendo:\n';
    results += '     docker-compose ps\n';
    results += '   • Verificar logs del Classroom Service:\n';
    results += '     docker-compose logs classroom-service\n';
    results += '   • Probar endpoint manualmente:\n';
    results += '     curl http://localhost:8081/api/classrooms\n';
    results += '   • Verificar que puerto 8081 esté accesible desde browser\n';
    
  } catch (error) {
    results += `❌ Diagnostic failed: ${error.message}\n`;
  }
  
  content.textContent = results;
}

function hideDiagnostic() {
  document.getElementById('diagnosticPanel').style.display = 'none';
}

// ===================================
// �📅 ESTABLECER FECHA MÍNIMA (HOY)
// ===================================
function setMinDate() {
  const today = new Date().toISOString().split('T')[0];
  document.getElementById("loanDate").setAttribute('min', today);
}

// ===================================
// 🏫 CARGAR AULAS DISPONIBLES DESDE CLASSROOM SERVICE
// ===================================
async function loadClassrooms() {
  try {
    console.log("🔄 Cargando aulas desde Classroom Service...");
    console.log("📡 URL del Classroom Service:", CLASSROOM_API_URL);
    
    // Verificar que el select existe
    if (!classroomSelect) {
      console.error("❌ No se encontró el elemento select con ID 'classroomCode'");
      return;
    }
    
    // Mostrar indicador de carga
    classroomSelect.innerHTML = '<option value="">Loading classrooms...</option>';
    
    const response = await fetch(CLASSROOM_API_URL, {
      method: 'GET',
      headers: {
        'Accept': 'application/json',
        'Content-Type': 'application/json'
      }
    });
    
    console.log("📊 Response status:", response.status);
    console.log("📊 Response headers:", Object.fromEntries(response.headers.entries()));
    
    if (!response.ok) {
      throw new Error(`HTTP error! status: ${response.status} - ${response.statusText}`);
    }
    
    const classrooms = await response.json();
    console.log("✅ Aulas cargadas:", classrooms);
    console.log("📊 Número de aulas:", classrooms.length);
    
    // Verificar que classrooms es un array
    if (!Array.isArray(classrooms)) {
      throw new Error("La respuesta no es un array válido: " + typeof classrooms);
    }
    
    // Limpiar y llenar el select
    classroomSelect.innerHTML = '<option value="">Select a classroom...</option>';
    
    if (classrooms.length === 0) {
      classroomSelect.innerHTML += '<option value="" disabled>No classrooms available</option>';
      console.warn("⚠️ No hay aulas disponibles en la respuesta");
      return;
    }
    
    classrooms.forEach((classroom, index) => {
      console.log(`🏫 Procesando aula ${index + 1}:`, classroom);
      
      const option = document.createElement("option");
      option.value = classroom.id;
      option.textContent = `${classroom.name} - ${classroom.location} (Capacity: ${classroom.capacity})`;
      
      // Deshabilitar aulas no disponibles
      if (classroom.state !== "AVAILABLE") {
        option.disabled = true;
        option.textContent += ` - ${classroom.state}`;
        console.log(`⚠️ Aula ${classroom.name} no disponible - Estado: ${classroom.state}`);
      } else {
        console.log(`✅ Aula ${classroom.name} disponible`);
      }
      
      classroomSelect.appendChild(option);
    });
    
    console.log("✅ Select de aulas poblado correctamente con", classrooms.length, "opciones");
  } catch (error) {
    console.error("❌ Error detallado cargando aulas:", error);
    console.error("❌ Error stack:", error.stack);
    classroomSelect.innerHTML = '<option value="">Error loading classrooms - Check console</option>';
    
    // Mostrar error más específico al usuario
    let errorMessage = "⚠️ Could not load classrooms.\n\n";
    
    if (error.name === 'TypeError' && error.message.includes('fetch')) {
      errorMessage += "• Classroom Service might not be running\n";
      errorMessage += "• Check if http://localhost:8081 is accessible\n";
      errorMessage += "• Verify Docker containers are up";
    } else if (error.message.includes('CORS')) {
      errorMessage += "• CORS error - Check @CrossOrigin annotation\n";
      errorMessage += "• Verify ClassroomController has CORS enabled";
    } else if (error.message.includes('404')) {
      errorMessage += "• Endpoint not found\n";
      errorMessage += "• Verify /api/classrooms endpoint exists";
    } else {
      errorMessage += "• " + error.message;
    }
    
    alert(errorMessage);
  }
}

// ===================================
// 📋 ENVIAR FORMULARIO (CREATE/UPDATE)
// ===================================
loanForm.addEventListener("submit", async (e) => {
  e.preventDefault();

  const loanData = {
    userCode: document.getElementById("userCode").value,
    classroomCode: parseInt(document.getElementById("classroomCode").value),
    loanDate: document.getElementById("loanDate").value,
    startTime: document.getElementById("startTime").value,
    endTime: document.getElementById("endTime").value,
    purpose: document.getElementById("purpose").value,
    status: document.getElementById("status").value
  };

  // Validación: hora de fin debe ser mayor que hora de inicio
  if (loanData.startTime >= loanData.endTime) {
    alert("⚠️ End time must be after start time!");
    return;
  }

  try {
    let response;
    
    if (editingId) {
      // Actualizar préstamo existente
      response = await fetch(`${LOAN_API_URL}/${editingId}`, {
        method: "PUT",
        headers: { "Content-Type": "application/json" },
        body: JSON.stringify(loanData)
      });
    } else {
      // Crear nuevo préstamo
      response = await fetch(LOAN_API_URL, {
        method: "POST",
        headers: { "Content-Type": "application/json" },
        body: JSON.stringify(loanData)
      });
    }

    if (!response.ok) {
      const errorData = await response.json();
      throw new Error(errorData.message || "Error saving loan");
    }

    const result = await response.json();
    console.log("✅ Loan saved:", result);
    
    alert(editingId ? "✅ Loan updated successfully!" : "✅ Loan created successfully!");
    resetForm();
    loadLoans();
    
  } catch (error) {
    console.error("❌ Error saving loan:", error);
    alert("❌ Error: " + error.message);
  }
});

// ===================================
// 📄 CARGAR TODOS LOS PRÉSTAMOS
// ===================================
async function loadLoans() {
  try {
    const response = await fetch(LOAN_API_URL);
    const loans = await response.json();

    loanBody.innerHTML = "";

    if (loans.length === 0) {
      loanBody.innerHTML = '<tr><td colspan="9" class="no-data">No loans found</td></tr>';
      return;
    }

    loans.forEach(loan => {
      const tr = document.createElement("tr");
      tr.className = `status-${loan.status.toLowerCase()}`;
      
      tr.innerHTML = `
        <td>${loan.id}</td>
        <td>${loan.userCode}</td>
        <td>${loan.classroomCode}</td>
        <td>${formatDate(loan.loanDate)}</td>
        <td>${loan.startTime}</td>
        <td>${loan.endTime}</td>
        <td class="purpose-cell">${loan.purpose}</td>
        <td><span class="status-badge ${loan.status.toLowerCase()}">${loan.status}</span></td>
        <td class="action-buttons">
          <button class="btn-edit" onclick="editLoan(${loan.id})" title="Edit">✏️</button>
          <button class="btn-delete" onclick="deleteLoan(${loan.id})" title="Delete">🗑️</button>
          ${getStatusActions(loan)}
        </td>
      `;
      loanBody.appendChild(tr);
    });
  } catch (error) {
    console.error("❌ Error loading loans:", error);
    loanBody.innerHTML = '<tr><td colspan="9" class="error">Error loading loans</td></tr>';
  }
}

// ===================================
// 🎯 BOTONES DE ACCIÓN POR ESTADO
// ===================================
function getStatusActions(loan) {
  let buttons = '';
  
  if (loan.status === 'RESERVED') {
    buttons += `<button class="btn-activate" onclick="activateLoan(${loan.id})" title="Activate">✔️ Activate</button>`;
  }
  
  if (loan.status !== 'CANCELLED') {
    buttons += `<button class="btn-cancel" onclick="cancelLoan(${loan.id})" title="Cancel">❌ Cancel</button>`;
  }
  
  return buttons;
}

// ===================================
// ✏️ EDITAR PRÉSTAMO
// ===================================
async function editLoan(id) {
  try {
    const response = await fetch(`${LOAN_API_URL}/${id}`);
    const loan = await response.json();

    document.getElementById("userCode").value = loan.userCode;
    document.getElementById("classroomCode").value = loan.classroomCode;
    document.getElementById("loanDate").value = loan.loanDate;
    document.getElementById("startTime").value = loan.startTime;
    document.getElementById("endTime").value = loan.endTime;
    document.getElementById("purpose").value = loan.purpose;
    document.getElementById("status").value = loan.status;

    editingId = id;
    saveBtn.textContent = "Update Loan";
    
    // Scroll al formulario
    loanForm.scrollIntoView({ behavior: 'smooth' });
  } catch (error) {
    console.error("❌ Error loading loan:", error);
    alert("Error loading loan data");
  }
}

// ===================================
// 🗑️ ELIMINAR PRÉSTAMO
// ===================================
async function deleteLoan(id) {
  if (!confirm("Are you sure you want to delete this loan?")) return;

  try {
    const response = await fetch(`${LOAN_API_URL}/${id}`, { method: "DELETE" });
    
    if (response.ok) {
      alert("✅ Loan deleted successfully!");
      loadLoans();
    } else {
      throw new Error("Failed to delete loan");
    }
  } catch (error) {
    console.error("❌ Error deleting loan:", error);
    alert("Error deleting loan");
  }
}

// ===================================
// ✅ ACTIVAR PRÉSTAMO
// ===================================
async function activateLoan(id) {
  try {
    const response = await fetch(`${LOAN_API_URL}/${id}/activate`, { method: "PATCH" });
    
    if (response.ok) {
      alert("✅ Loan activated successfully!");
      loadLoans();
    } else {
      const error = await response.json();
      throw new Error(error.message);
    }
  } catch (error) {
    console.error("❌ Error activating loan:", error);
    alert("Error: " + error.message);
  }
}

// ===================================
// ❌ CANCELAR PRÉSTAMO
// ===================================
async function cancelLoan(id) {
  if (!confirm("Are you sure you want to cancel this loan?")) return;

  try {
    const response = await fetch(`${LOAN_API_URL}/${id}/cancel`, { method: "PATCH" });
    
    if (response.ok) {
      alert("✅ Loan cancelled successfully!");
      loadLoans();
    } else {
      const error = await response.json();
      throw new Error(error.message);
    }
  } catch (error) {
    console.error("❌ Error cancelling loan:", error);
    alert("Error: " + error.message);
  }
}

// ===================================
// 🔄 RESETEAR FORMULARIO
// ===================================
function resetForm() {
  loanForm.reset();
  editingId = null;
  saveBtn.textContent = "Add Loan";
  setMinDate();
}

// ===================================
// 🔍 FILTRAR POR ESTADO
// ===================================
async function filterByStatus() {
  const status = document.getElementById("statusFilter").value;
  
  try {
    let url = LOAN_API_URL;
    
    if (status) {
      url += `/status/${status}`;
    }
    
    const response = await fetch(url);
    const loans = await response.json();
    
    loanBody.innerHTML = "";
    
    if (loans.length === 0) {
      loanBody.innerHTML = '<tr><td colspan="9" class="no-data">No loans found with this status</td></tr>';
      return;
    }
    
    loans.forEach(loan => {
      const tr = document.createElement("tr");
      tr.className = `status-${loan.status.toLowerCase()}`;
      
      tr.innerHTML = `
        <td>${loan.id}</td>
        <td>${loan.userCode}</td>
        <td>${loan.classroomCode}</td>
        <td>${formatDate(loan.loanDate)}</td>
        <td>${loan.startTime}</td>
        <td>${loan.endTime}</td>
        <td class="purpose-cell">${loan.purpose}</td>
        <td><span class="status-badge ${loan.status.toLowerCase()}">${loan.status}</span></td>
        <td class="action-buttons">
          <button class="btn-edit" onclick="editLoan(${loan.id})">✏️</button>
          <button class="btn-delete" onclick="deleteLoan(${loan.id})">🗑️</button>
          ${getStatusActions(loan)}
        </td>
      `;
      loanBody.appendChild(tr);
    });
  } catch (error) {
    console.error("❌ Error filtering loans:", error);
  }
}

// ===================================
// 📅 FORMATEAR FECHA
// ===================================
function formatDate(dateString) {
  const date = new Date(dateString + 'T00:00:00');
  return date.toLocaleDateString('es-ES', { 
    year: 'numeric', 
    month: '2-digit', 
    day: '2-digit' 
  });
}
