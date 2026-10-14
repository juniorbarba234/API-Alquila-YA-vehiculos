const datos = { oficinas: [], clientes: [], vehiculos: [], alquileres: [] };
const claves = { oficinas: 'idOficina', clientes: 'idCliente', vehiculos: 'idVehiculo', alquileres: 'idAlquiler' };
const mensaje = document.querySelector('#mensaje');
const dinero = valor => Number(valor).toLocaleString('es-MX', { minimumFractionDigits: 2, maximumFractionDigits: 2 });

function avisar(texto, error = false) {
  mensaje.textContent = texto;
  mensaje.classList.toggle('error', error);
}

async function solicitar(ruta, metodo = 'GET', cuerpo) {
  let respuesta;
  try {
    respuesta = await fetch('/api/' + ruta, {
      method: metodo,
      headers: { 'Content-Type': 'application/json' },
      body: cuerpo === undefined ? undefined : JSON.stringify(cuerpo)
    });
  } catch {
    throw new Error('No se pudo conectar. Comprueba que AlquilaYa esté encendido y vuelve a actualizar.');
  }
  const texto = await respuesta.text();
  let resultado = texto;
  try { resultado = texto ? JSON.parse(texto) : null; } catch {}
  if (!respuesta.ok) {
    const detalle = typeof resultado === 'string' ? resultado : resultado?.message;
    throw new Error(detalle || 'No se pudo guardar el cambio. Revisa los datos: puede haber un registro duplicado o relacionado.');
  }
  return resultado;
}

function opciones(selector, registros, clave, etiqueta, vacio) {
  const select = document.querySelector(selector);
  const anterior = select.value;
  select.replaceChildren(new Option(registros.length ? 'Selecciona una opción' : vacio, ''));
  registros.forEach(item => select.add(new Option(etiqueta(item), item[clave])));
  if (registros.some(item => String(item[clave]) === anterior)) select.value = anterior;
}

function boton(texto, accion, clase = 'secundario') {
  const elemento = document.createElement('button');
  elemento.type = 'button';
  elemento.textContent = texto;
  elemento.className = clase;
  elemento.addEventListener('click', async () => {
    elemento.disabled = true;
    try { await accion(); } catch (error) { avisar(error.message, true); }
    finally { elemento.disabled = false; }
  });
  return elemento;
}

function tabla(tipo, valores) {
  const cuerpo = document.querySelector('#lista-' + tipo);
  cuerpo.replaceChildren();
  if (!datos[tipo].length) {
    const celda = cuerpo.insertRow().insertCell();
    celda.colSpan = document.querySelector('#' + tipo + ' thead tr').children.length;
    celda.textContent = 'Todavía no hay registros.';
    celda.className = 'vacio';
  }
  datos[tipo].forEach(item => {
    const fila = cuerpo.insertRow();
    valores(item).forEach(valor => { fila.insertCell().textContent = valor ?? '—'; });
    const acciones = fila.insertCell();
    acciones.className = 'acciones';
    if (tipo === 'alquileres') {
      if (item.estado === 'Activo') acciones.append(boton('Completar', async () => {
        if (!confirm('¿Registrar la devolución y completar este alquiler?')) return;
        await solicitar('alquileres/' + item.idAlquiler + '/completar', 'PUT');
        await refrescar('Alquiler completado. El vehículo vuelve a estar disponible.');
      }));
      else acciones.textContent = 'Finalizado';
    } else {
      if (tipo === 'vehiculos' && item.estado === 'Alquilado') { acciones.textContent = 'En alquiler'; return; }
      acciones.append(boton('Editar', () => editar(tipo, item)), boton('Eliminar', async () => {
        if (!confirm('¿Eliminar este registro? Esta acción no se puede deshacer.')) return;
        await solicitar(tipo + '/' + item[claves[tipo]], 'DELETE');
        limpiar(document.querySelector('#form-' + tipo));
        await refrescar('Registro eliminado.');
      }, 'peligro'));
    }
  });
}

function mostrar() {
  opciones('#form-vehiculos [name=oficina]', datos.oficinas, 'idOficina', o => o.nombre, 'Registra primero una oficina');
  opciones('#form-alquileres [name=cliente]', datos.clientes, 'idCliente', c => c.nombre + ' ' + c.apellido + ' · ' + c.email, 'Registra primero un cliente');
  opciones('#form-alquileres [name=vehiculo]', datos.vehiculos.filter(v => v.estado === 'Disponible'), 'idVehiculo', v => v.matricula + ' · ' + v.marca + ' ' + v.modelo + ' · ' + dinero(v.tarifaDiaria) + '/día', 'No hay vehículos disponibles');
  tabla('oficinas', o => [o.nombre, o.direccion, o.ciudad, o.region]);
  tabla('clientes', c => [c.nombre + ' ' + c.apellido, c.dni, c.email, c.licenciaConducir, c.telefono]);
  tabla('vehiculos', v => [v.matricula, v.marca + ' ' + v.modelo, v.anio, dinero(v.tarifaDiaria), v.oficina?.nombre, v.estado]);
  tabla('alquileres', a => [a.idAlquiler, a.cliente?.nombre + ' ' + a.cliente?.apellido, a.vehiculo?.matricula, a.fechaAlquiler, a.fechaDevolucion, dinero(a.precioTotal), a.estado]);
  estimar();
}

async function cargar() {
  const tipos = Object.keys(datos);
  const listas = await Promise.all(tipos.map(tipo => solicitar(tipo)));
  listas.forEach((lista, indice) => { datos[tipos[indice]] = lista; });
  mostrar();
}

async function refrescar(exito = 'Datos actualizados.') {
  try { await cargar(); avisar(exito); }
  catch (error) { avisar(exito + ' No se pudo actualizar la lista. Pulsa Actualizar datos. ' + error.message, true); }
}

function limpiar(form) {
  form.reset();
  delete form.dataset.id;
  const cancelar = form.querySelector('.cancelar');
  if (cancelar) cancelar.hidden = true;
  form.querySelector('h3').textContent = form.dataset.titulo;
}

function editar(tipo, item) {
  const form = document.querySelector('#form-' + tipo);
  form.dataset.id = item[claves[tipo]];
  for (const campo of form.elements) {
    if (!campo.name) continue;
    campo.value = campo.name === 'oficina' ? item.oficina?.idOficina : item[campo.name] ?? '';
  }
  form.querySelector('h3').textContent = 'Editar registro #' + item[claves[tipo]];
  form.querySelector('.cancelar').hidden = false;
  form.scrollIntoView({ behavior: 'smooth', block: 'start' });
  form.querySelector('input').focus();
}

function estimar() {
  const form = document.querySelector('#form-alquileres');
  const campos = form.elements;
  campos.fechaDevolucion.setCustomValidity('');
  const dias = (Date.parse(campos.fechaDevolucion.value) - Date.parse(campos.fechaAlquiler.value)) / 86400000;
  if (Number.isFinite(dias) && dias <= 0) campos.fechaDevolucion.setCustomValidity('La devolución debe ser posterior al alquiler.');
  const vehiculo = datos.vehiculos.find(v => String(v.idVehiculo) === campos.vehiculo.value);
  document.querySelector('#estimacion').textContent = vehiculo && dias > 0
    ? 'Total estimado: ' + dinero(dias * vehiculo.tarifaDiaria) + ' (' + dias + ' días).'
    : 'El total se calcula según los días y la tarifa diaria.';
}

document.querySelectorAll('nav button').forEach(b => b.addEventListener('click', () => {
  document.querySelectorAll('main > section').forEach(s => { s.hidden = s.id !== b.dataset.seccion; });
  document.querySelectorAll('nav button').forEach(n => n.setAttribute('aria-pressed', String(n === b)));
}));

document.querySelectorAll('form').forEach(form => {
  form.dataset.titulo = form.querySelector('h3').textContent;
  form.querySelector('.cancelar')?.addEventListener('click', () => limpiar(form));
  form.addEventListener('submit', async event => {
    event.preventDefault();
    const enviar = form.querySelector('[type=submit]');
    enviar.disabled = true;
    const tipo = form.id.replace('form-', '');
    let cuerpo = Object.fromEntries(new FormData(form));
    Object.keys(cuerpo).forEach(k => { cuerpo[k] = cuerpo[k].trim(); });
    if (tipo === 'vehiculos') cuerpo = { ...cuerpo, anio: Number(cuerpo.anio), tarifaDiaria: Number(cuerpo.tarifaDiaria), oficina: { idOficina: Number(cuerpo.oficina) } };
    if (tipo === 'alquileres') cuerpo = { ...cuerpo, cliente: { idCliente: Number(cuerpo.cliente) }, vehiculo: { idVehiculo: Number(cuerpo.vehiculo) } };
    try {
      const ruta = tipo + (form.dataset.id ? '/' + form.dataset.id : '');
      await solicitar(ruta, form.dataset.id ? 'PUT' : 'POST', cuerpo);
      limpiar(form);
      await refrescar('Registro guardado correctamente.');
    } catch (error) { avisar(error.message, true); }
    finally { enviar.disabled = false; }
  });
});
document.querySelector('#form-alquileres').addEventListener('input', estimar);
document.querySelector('#actualizar').addEventListener('click', async event => {
  event.target.disabled = true;
  try { await cargar(); avisar('Datos actualizados.'); } catch (error) { avisar(error.message, true); }
  finally { event.target.disabled = false; }
});
cargar().then(() => avisar('')).catch(error => avisar(error.message, true));
