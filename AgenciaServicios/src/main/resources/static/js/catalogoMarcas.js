document.addEventListener("DOMContentLoaded", () => {
  const tablaBody = document.querySelector("tbody");
  const modal = document.getElementById("modalMarca");
  const form = document.getElementById("formMarca");
  const inputNombre = document.getElementById("inputMarca");
  const checkActivo = document.getElementById("checkActivo");

  let idEditando = null;
  const API = "http://localhost:8080/api/marcas";

  // === Cargar todas las marcas ===
  const cargarMarcas = async () => {
    try {
      const res = await fetch(API);
      const data = await res.json();

      tablaBody.innerHTML = "";
      data.forEach((marca) => {
        const fila = document.createElement("tr");
        fila.className =
          "border-b border-gray-800 hover:bg-black/40 transition-all";
        fila.innerHTML = `
          <td class="py-2">${marca.idMarca}</td>
          <td>${marca.nombreMarca}</td>
          <td class="${
            marca.activo ? "text-green-400" : "text-red-400"
          }">${marca.activo ? "Sí" : "No"}</td>
          <td class="text-center flex gap-2 justify-center py-2">
            <button onclick='editarMarca(${JSON.stringify(marca)
              .replace(/'/g, "&apos;")
              .replace(/"/g, "&quot;")})'>
              <i class="bx bx-edit text-blue-400 text-xl hover:scale-110 transition"></i>
            </button>
            <button onclick="eliminarMarca(${marca.idMarca})">
              <i class="bx bx-trash text-red-500 text-xl hover:scale-110 transition"></i>
            </button>
          </td>`;
        tablaBody.appendChild(fila);
      });
    } catch (err) {
      console.error("Error al cargar marcas:", err);
    }
  };

  cargarMarcas();

  // === Abrir / cerrar modal ===
  window.abrirModalMarca = () => {
    idEditando = null;
    form.reset();
    modal.classList.remove("hidden");
    modal.classList.add("flex");
    modal.querySelector("h2").textContent = "Nueva Marca";
  };

  window.cerrarModalMarca = () => {
    modal.classList.add("hidden");
    modal.classList.remove("flex");
  };

  // === Guardar o Actualizar Marca ===
  form.addEventListener("submit", async (e) => {
    e.preventDefault();

    if (!inputNombre.value.trim()) {
      alert("⚠️ El nombre de la marca es obligatorio");
      return;
    }

    const marca = {
      nombreMarca: inputNombre.value,
      activo: checkActivo ? checkActivo.checked : true,
    };

    try {
      let res;
      if (idEditando) {
        // Actualizar marca existente
        res = await fetch(`${API}/${idEditando}`, {
          method: "PUT",
          headers: { "Content-Type": "application/json" },
          body: JSON.stringify(marca),
        });
      } else {
        // Crear nueva marca
        res = await fetch(API, {
          method: "POST",
          headers: { "Content-Type": "application/json" },
          body: JSON.stringify(marca),
        });
      }

      if (!res.ok) {
        const mensaje = await res.text();
        alert("⚠️ " + mensaje);
        return;
      }

      cerrarModalMarca();
      form.reset();
      await cargarMarcas();

      alert(idEditando ? "Marca actualizada correctamente" : "Marca registrada con éxito");
      idEditando = null;
    } catch (err) {
      console.error("Error al guardar marca:", err);
    }
  });

  // === Editar marca ===
  window.editarMarca = (marca) => {
    idEditando = marca.idMarca;
    inputNombre.value = marca.nombreMarca;
    if (checkActivo) checkActivo.checked = marca.activo;

    modal.classList.remove("hidden");
    modal.classList.add("flex");
    modal.querySelector("h2").textContent = "Editar Marca";
  };

  // === Eliminar marca ===
  window.eliminarMarca = async (id) => {
    if (!confirm("¿Deseas eliminar esta marca?")) return;

    try {
      const res = await fetch(`${API}/${id}`, {
        method: "DELETE",
      });

      if (res.ok) {
        await cargarMarcas();
        alert("Marca eliminada correctamente");
      } else {
        console.error("Error al eliminar marca");
      }
    } catch (err) {
      console.error(err);
    }
  };
});
