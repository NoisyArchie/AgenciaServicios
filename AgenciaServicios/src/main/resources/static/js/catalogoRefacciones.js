document.addEventListener("DOMContentLoaded", () => {
  const tablaBody = document.querySelector("tbody");
  const modal = document.getElementById("modalRefaccion");
  const form = document.getElementById("formRefaccion");
  const nombre = document.getElementById("inputNombre");
  const descripcion = document.getElementById("inputDescripcion");
  const precio = document.getElementById("inputPrecio");
  const stock = document.getElementById("inputStock");
  const activo = document.getElementById("checkActivo");

  let idEditando = null;

  const API = "http://localhost:8080/api/refacciones";

  // === Cargar Refacciones ===
  const cargarRefacciones = async () => {
    try {
      const res = await fetch(API);
      const data = await res.json();
      tablaBody.innerHTML = "";

      data.forEach((r) => {
        const fila = document.createElement("tr");
        fila.className = "border-b border-gray-800 hover:bg-black/40 transition";
        fila.innerHTML = `
          <td class="py-2">${r.idRefaccion}</td>
          <td>${r.nombreRefaccion}</td>
          <td>${r.descripcion || "-"}</td>
          <td>$${parseFloat(r.precioUnitario || 0).toFixed(2)}</td>
          <td class="flex items-center justify-center gap-2 py-2">
            <button onclick="ajustarStock(${r.idRefaccion}, -1)" class="px-2 py-1 bg-gray-700 hover:bg-gray-600 rounded">
              <i class="bx bx-minus"></i>
            </button>
            <span class="min-w-[40px] text-center">${r.stock}</span>
            <button onclick="ajustarStock(${r.idRefaccion}, 1)" class="px-2 py-1 bg-gray-700 hover:bg-gray-600 rounded">
              <i class="bx bx-plus"></i>
            </button>
          </td>
          <td class="${r.activo ? "text-green-400" : "text-red-400"}">
            ${r.activo ? "Sí" : "No"}
          </td>
          <td class="text-center flex gap-2 justify-center py-2">
            <button onclick='editarRefaccion(${JSON.stringify(r)
              .replace(/'/g, "&apos;")
              .replace(/"/g, "&quot;")})'>
              <i class="bx bx-edit text-blue-400 text-xl hover:scale-110 transition"></i>
            </button>
            <button onclick="eliminarRefaccion(${r.idRefaccion})">
              <i class="bx bx-trash text-red-500 text-xl hover:scale-110 transition"></i>
            </button>
          </td>`;
        tablaBody.appendChild(fila);
      });
    } catch (err) {
      console.error("Error al cargar refacciones:", err);
    }
  };

  cargarRefacciones();

  // === Mostrar / Ocultar modal ===
  window.abrirModalRefaccion = () => {
    idEditando = null;
    form.reset();
    modal.classList.remove("hidden");
    modal.classList.add("flex");
    modal.querySelector("h2").textContent = "Nueva Refacción";
  };

  window.cerrarModalRefaccion = () => {
    modal.classList.add("hidden");
    modal.classList.remove("flex");
  };

  // === Crear / Actualizar refacción ===
  form.addEventListener("submit", async (e) => {
    e.preventDefault();

    if (!nombre.value.trim() || !precio.value.trim()) {
      alert("El nombre y el precio son obligatorios");
      return;
    }

    const refaccion = {
      nombreRefaccion: nombre.value,
      descripcion: descripcion.value,
      precioUnitario: parseFloat(precio.value),
      stock: parseInt(stock.value) || 0,
      activo: activo.checked,
    };

    try {
      let res;
      if (idEditando) {
        res = await fetch(`${API}/${idEditando}`, {
          method: "PUT",
          headers: { "Content-Type": "application/json" },
          body: JSON.stringify(refaccion),
        });
      } else {
        res = await fetch(API, {
          method: "POST",
          headers: { "Content-Type": "application/json" },
          body: JSON.stringify(refaccion),
        });
      }

      if (!res.ok) {
        const mensaje = await res.text();
        alert("Error: " + mensaje);
        return;
      }

      cerrarModalRefaccion();
      form.reset();
      await cargarRefacciones();
      alert(idEditando ? "Refacción actualizada correctamente" : "Refacción registrada");
      idEditando = null;
    } catch (err) {
      console.error("Error al guardar refacción:", err);
    }
  });

  // === Editar refacción ===
  window.editarRefaccion = (r) => {
    idEditando = r.idRefaccion;
    nombre.value = r.nombreRefaccion;
    descripcion.value = r.descripcion || "";
    precio.value = r.precioUnitario;
    stock.value = r.stock;
    activo.checked = r.activo;

    modal.classList.remove("hidden");
    modal.classList.add("flex");
    modal.querySelector("h2").textContent = "Editar Refacción";
  };

  // === Ajustar stock (botones + / -) ===
  window.ajustarStock = async (id, cambio) => {
    try {
      const res = await fetch(`${API}/${id}`);
      if (!res.ok) return;
      const ref = await res.json();

      const nuevoStock = Math.max(0, ref.stock + cambio);

      const actualizado = { ...ref, stock: nuevoStock };
      const resUpdate = await fetch(`${API}/${id}`, {
        method: "PUT",
        headers: { "Content-Type": "application/json" },
        body: JSON.stringify(actualizado),
      });

      if (resUpdate.ok) await cargarRefacciones();
    } catch (err) {
      console.error("Error al ajustar stock:", err);
    }
  };

  // === Eliminar refacción ===
  window.eliminarRefaccion = async (id) => {
    if (!confirm("¿Deseas eliminar esta refacción?")) return;
    try {
      const res = await fetch(`${API}/${id}`, { method: "DELETE" });
      if (res.ok) await cargarRefacciones();
    } catch (err) {
      console.error("Error al eliminar refacción:", err);
    }
  };
});
