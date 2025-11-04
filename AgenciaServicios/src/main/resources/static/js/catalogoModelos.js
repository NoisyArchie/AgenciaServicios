document.addEventListener("DOMContentLoaded", () => {
  const tablaBody = document.querySelector("tbody");
  const modal = document.getElementById("modalModelo");
  const form = document.getElementById("formModelo");
  const selectMarca = document.getElementById("selectMarca");
  const inputNombre = document.getElementById("inputNombreModelo");
  const checkActivo = document.getElementById("checkActivo");

  const API_MODELOS = "http://localhost:8080/api/modelos";
  const API_MARCAS = "http://localhost:8080/api/marcas";

  // === Cargar marcas para el <select> ===
  const cargarMarcas = async () => {
    try {
      const res = await fetch(API_MARCAS);
      const marcas = await res.json();
      selectMarca.innerHTML = marcas
        .filter((m) => m.activo)
        .map((m) => `<option value="${m.idMarca}">${m.nombreMarca}</option>`)
        .join("");
    } catch (err) {
      console.error("Error al cargar marcas:", err);
    }
  };

  // === Cargar modelos ===
  const cargarModelos = async () => {
    try {
      const res = await fetch(API_MODELOS);
      const data = await res.json();
      tablaBody.innerHTML = "";

      data.forEach((modelo) => {
        const fila = document.createElement("tr");
        fila.className = "border-b border-gray-800 hover:bg-black/40 transition";
        fila.innerHTML = `
          <td class="py-2">${modelo.idModelo}</td>
          <td>${modelo.marca ? modelo.marca.nombreMarca : "—"}</td>
          <td>${modelo.nombreModelo}</td>
          <td class="${modelo.activo ? "text-green-400" : "text-red-400"}">
            ${modelo.activo ? "Sí" : "No"}
          </td>
          <td class="text-center flex gap-2 justify-center py-2">
            <button onclick="editarModelo(${modelo.idModelo}, '${modelo.nombreModelo}', ${modelo.marca?.idMarca || 0}, ${modelo.activo})">
              <i class="bx bx-edit text-blue-400 text-xl hover:scale-110 transition"></i>
            </button>
            <button onclick="eliminarModelo(${modelo.idModelo})">
              <i class="bx bx-trash text-red-500 text-xl hover:scale-110 transition"></i>
            </button>
          </td>`;
        tablaBody.appendChild(fila);
      });
    } catch (err) {
      console.error("Error al cargar modelos:", err);
    }
  };

  // === Mostrar / Ocultar modal ===
  window.abrirModalModelo = async () => {
    await cargarMarcas();
    modal.classList.remove("hidden");
    modal.classList.add("flex");
    form.reset();
  };

  window.cerrarModalModelo = () => {
    modal.classList.add("hidden");
    modal.classList.remove("flex");
  };

  // === Crear nuevo modelo ===
  form.addEventListener("submit", async (e) => {
    e.preventDefault();

    const nuevo = {
      nombreModelo: inputNombre.value.trim(),
      activo: checkActivo.checked,
      marca: { idMarca: parseInt(selectMarca.value) },
    };

    if (!nuevo.nombreModelo) {
      alert("El nombre del modelo es obligatorio");
      return;
    }

    try {
      const res = await fetch(API_MODELOS, {
        method: "POST",
        headers: { "Content-Type": "application/json" },
        body: JSON.stringify(nuevo),
      });

      if (!res.ok) {
        const msg = await res.text();
        alert("⚠️ " + msg);
        return;
      }

      cerrarModalModelo();
      form.reset();
      await cargarModelos();
      alert("Modelo guardado correctamente");
    } catch (err) {
      console.error("Error al guardar modelo:", err);
    }
  });

  // === Editar modelo ===
  window.editarModelo = async (id, nombre, idMarca, activo) => {
    await cargarMarcas();
    inputNombre.value = nombre;
    selectMarca.value = idMarca;
    checkActivo.checked = activo;
    modal.classList.remove("hidden");
    modal.classList.add("flex");

    // cambiar botón Guardar a "Actualizar"
    form.onsubmit = async (e) => {
      e.preventDefault();
      const actualizado = {
        idModelo: id,
        nombreModelo: inputNombre.value.trim(),
        activo: checkActivo.checked,
        marca: { idMarca: parseInt(selectMarca.value) },
      };

      const res = await fetch(`${API_MODELOS}/${id}`, {
        method: "PUT",
        headers: { "Content-Type": "application/json" },
        body: JSON.stringify(actualizado),
      });

      if (res.ok) {
        cerrarModalModelo();
        await cargarModelos();
        alert("Modelo actualizado correctamente");
      } else {
        const msg = await res.text();
        alert("Error: " + msg);
      }
    };
  };

  // === Eliminar modelo ===
  window.eliminarModelo = async (id) => {
    if (!confirm("¿Deseas eliminar este modelo?")) return;
    try {
      const res = await fetch(`${API_MODELOS}/${id}`, {
        method: "DELETE",
      });
      if (res.ok) await cargarModelos();
    } catch (err) {
      console.error("Error al eliminar modelo:", err);
    }
  };

  // === Inicializar ===
  cargarModelos();
});
