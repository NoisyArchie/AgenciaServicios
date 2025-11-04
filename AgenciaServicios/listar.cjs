const fs = require('fs');
const path = require('path');

function listar(dir, nivel = 0) {
  const espacios = '│   '.repeat(nivel);
  for (const item of fs.readdirSync(dir)) {
    if (item === 'node_modules') continue; // excluir node_modules
    const ruta = path.join(dir, item);
    const stats = fs.statSync(ruta);

    if (stats.isDirectory()) {
      console.log(`${espacios}├───${item}`);
      listar(ruta, nivel + 1);
    } else {
      console.log(`${espacios}│   ${item}`);
    }
  }
}

listar('.'); // 👈 carpeta actual
