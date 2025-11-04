document.addEventListener("DOMContentLoaded", () => {
  const tablaBody = document.querySelector("tbody");
  const modal = document.getElementById("modalTipoServicio");
  const form = document.getElementById("formTipoServicio");
  const nombre = document.getElementById("inputNombre");
  const descripcion = document.getElementById("inputDescripcion");
  const costo = document.getElementById("inputCosto");
  const activo = document.getElementById("checkActivo");

  // Variable global para saber si estamos editando
  let idEditando = null;

  // === Cargar Tipos de Servicio ===
  const cargarTipos = async () => {
    try {
      const res = await fetch("http://localhost:8080/api/tipos-servicio");
      const data = await res.json();
      tablaBody.innerHTML = "";

      data.forEach((t) => {
        const fila = document.createElement("tr");
        fila.className = "border-b border-gray-800 hover:bg-black/40";
        fila.innerHTML = `
          <td class="py-2">${t.idTipoServicio}</td>
          <td>${t.nombreServicio}</td>
          <td>${t.descripcion || "-"}</td>
          <td>$${parseFloat(t.costoBase || 0).toFixed(2)}</td>
          <td class="${t.activo ? "text-green-400" : "text-red-400"}">${
          t.activo ? "Sí" : "No"
        }</td>
          <td class="text-center flex gap-2 justify-center py-2">
            <button onclick='editarTipo(${JSON.stringify(t)
              .replace(/'/g, "&apos;")
              .replace(/"/g, "&quot;")})'>
              <i class="bx bx-edit text-blue-400 text-xl hover:scale-110 transition"></i>
            </button>
            <button onclick="eliminarTipo(${t.idTipoServicio})">
              <i class="bx bx-trash text-red-500 text-xl hover:scale-110 transition"></i>
            </button>
          </td>`;
        tablaBody.appendChild(fila);
      });
    } catch (err) {
      console.error("Error al cargar tipos de servicio:", err);
    }
  };

  cargarTipos();

  // === Mostrar/Ocultar modal ===
  window.abrirModalTipo = () => {
    idEditando = null; // resetear modo edición
    form.reset();
    modal.classList.remove("hidden");
    modal.classList.add("flex");
    modal.querySelector("h2").textContent = "Nuevo Tipo de Servicio";
  };

  window.cerrarModalTipo = () => {
    modal.classList.add("hidden");
    modal.classList.remove("flex");
  };

  // === Guardar o Actualizar ===
  form.addEventListener("submit", async (e) => {
    e.preventDefault();

    if (!nombre.value.trim() || !costo.value.trim()) {
      alert("El nombre y el costo base son obligatorios");
      return;
    }

    const tipoServicio = {
      nombreServicio: nombre.value,
      descripcion: descripcion.value,
      costoBase: parseFloat(costo.value),
      activo: activo.checked,
    };

    try {
      let res;
      if (idEditando) {
        // === Actualizar existente ===
        res = await fetch(
          `http://localhost:8080/api/tipos-servicio/${idEditando}`,
          {
            method: "PUT",
            headers: { "Content-Type": "application/json" },
            body: JSON.stringify(tipoServicio),
          }
        );
      } else {
        // === Crear nuevo ===
        res = await fetch("http://localhost:8080/api/tipos-servicio", {
          method: "POST",
          headers: { "Content-Type": "application/json" },
          body: JSON.stringify(tipoServicio),
        });
      }

      if (!res.ok) {
        const mensaje = await res.text();
        alert("⚠️ " + mensaje);
        return;
      }

      cerrarModalTipo();
      form.reset();
      await cargarTipos();
      alert(idEditando ? "Tipo de servicio actualizado" : "Tipo de servicio creado");
      idEditando = null;
    } catch (err) {
      console.error("Error al guardar tipo de servicio:", err);
    }
  });

  // === Editar tipo ===
  window.editarTipo = (t) => {
    idEditando = t.idTipoServicio;
    nombre.value = t.nombreServicio;
    descripcion.value = t.descripcion || "";
    costo.value = t.costoBase;
    activo.checked = t.activo;

    modal.classList.remove("hidden");
    modal.classList.add("flex");
    modal.querySelector("h2").textContent = "Editar Tipo de Servicio";
  };

  // === Eliminar tipo ===
  window.eliminarTipo = async (id) => {
    if (!confirm("¿Deseas eliminar este tipo de servicio?")) return;
    try {
      const res = await fetch(
        `http://localhost:8080/api/tipos-servicio/${id}`,
        { method: "DELETE" }
      );
      if (res.ok) await cargarTipos();
      alert("🗑️ Tipo de servicio eliminado correctamente");
    } catch (err) {
      console.error("Error al eliminar tipo:", err);
    }
  };
});
