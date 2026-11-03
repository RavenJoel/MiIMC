// Vista del historial: consume el servicio REST y arma la tarjeta, la gráfica y la tabla.
(function () {
    const $ = (id) => document.getElementById(id);

    function insignia(clasificacion) {
        if (clasificacion === 'Bajo peso') return 'badge-b';
        if (clasificacion === 'Peso normal') return 'badge-n';
        if (clasificacion === 'Sobrepeso') return 'badge-s';
        return 'badge-o';
    }

    function fecha(iso, conHora) {
        const d = new Date(iso);
        const dia = d.toLocaleDateString('es-MX', {day: '2-digit', month: '2-digit', year: 'numeric'});
        return conHora ? dia + ' ' + d.toLocaleTimeString('es-MX', {hour: '2-digit', minute: '2-digit'}) : dia;
    }

    function texto(valor) {
        const span = document.createElement('span');
        span.textContent = valor;
        return span.innerHTML;
    }

    function grafica(mediciones) {
        const datos = mediciones.slice().reverse();          // de la más antigua a la más reciente
        const W = 700, H = 190, izq = 36, der = 16, arr = 12, aba = 28;
        const valores = datos.map(m => m.imc);
        let min = Math.min(...valores, 18.5) - 0.5, max = Math.max(...valores, 25) + 0.5;
        const x = (i) => datos.length === 1 ? (izq + W - der) / 2 : izq + i * (W - izq - der) / (datos.length - 1);
        const y = (v) => arr + (max - v) / (max - min) * (H - arr - aba);
        const banda = (desde, hasta, color) => {
            const y1 = y(Math.min(hasta, max)), y2 = y(Math.max(desde, min));
            return y2 > y1 ? `<rect x="${izq}" y="${y1}" width="${W - izq - der}" height="${y2 - y1}" fill="${color}"/>` : '';
        };
        let svg = `<svg width="100%" viewBox="0 0 ${W} ${H}" role="img" aria-label="Evolución del IMC">`;
        svg += banda(-100, 18.5, '#e7f0fb') + banda(18.5, 25, '#e6f5ed') + banda(25, 30, '#fdf3e1') + banda(30, 100, '#fbe5e5');
        [18.5, 25, 30].forEach(l => { if (l > min && l < max) {
            svg += `<line x1="${izq}" x2="${W - der}" y1="${y(l)}" y2="${y(l)}" stroke="#b9c2c8" stroke-dasharray="4 3"/>`;
            svg += `<text x="${izq - 4}" y="${y(l) + 4}" font-size="10" text-anchor="end" fill="#6b7780">${l}</text>`;
        }});
        const puntos = datos.map((m, i) => `${x(i)},${y(m.imc)}`).join(' ');
        if (datos.length > 1) svg += `<polyline points="${puntos}" fill="none" stroke="#1F6F5C" stroke-width="2.5"/>`;
        datos.forEach((m, i) => {
            svg += `<circle cx="${x(i)}" cy="${y(m.imc)}" r="4" fill="#1F6F5C"><title>${fecha(m.fecha)}: ${m.imc}</title></circle>`;
            svg += `<text x="${x(i)}" y="${H - 8}" font-size="10" text-anchor="middle" fill="#6b7780">${fecha(m.fecha).slice(0, 5)}</text>`;
        });
        $('grafica').innerHTML = svg + '</svg>';
    }

    function mostrar(datos) {
        if (datos.total === 0) {
            $('vacio').classList.remove('d-none');
            return;
        }
        const actual = datos.actual;
        $('fechaActual').textContent = fecha(actual.fecha);
        $('imcActual').textContent = actual.imc.toFixed(1);
        $('clasifActual').textContent = actual.clasificacion;
        $('clasifActual').classList.add(insignia(actual.clasificacion));
        const pos = Math.max(0, Math.min(100, (actual.imc - 15) / (40 - 15) * 100));
        $('marcador').style.left = `calc(${pos}% - 2px)`;
        if (datos.mediciones.length > 1) {
            const dif = actual.peso - datos.mediciones[1].peso;
            $('cambio').innerHTML = dif === 0 ? 'Mismo peso que en tu medición anterior.'
                : (dif < 0 ? '<i class="bi bi-arrow-down-right text-success"></i> Bajaste '
                           : '<i class="bi bi-arrow-up-right text-danger"></i> Subiste ')
                  + `<b>${Math.abs(dif).toFixed(1)} kg</b> desde tu medición anterior.`;
        }
        $('filas').innerHTML = datos.mediciones.map(m => `<tr>
            <td>${fecha(m.fecha, true)}</td><td>${m.peso.toFixed(1)} kg</td><td>${m.estatura.toFixed(2)} m</td>
            <td class="fw-bold">${m.imc.toFixed(1)}</td>
            <td><span class="badge ${insignia(m.clasificacion)}">${texto(m.clasificacion)}</span></td></tr>`).join('');
        grafica(datos.mediciones);
        $('contenido').classList.remove('d-none');
    }

    fetch(API, {headers: {'Accept': 'application/json'}, credentials: 'same-origin'})
        .then(async (resp) => {
            const datos = await resp.json();
            if (!resp.ok) throw new Error(datos.mensaje || 'No fue posible cargar el historial.');
            mostrar(datos);
        })
        .catch((e) => {
            $('error').innerHTML = '<i class="bi bi-exclamation-triangle-fill"></i> ' + texto(e.message);
            $('error').classList.remove('d-none');
        });
})();
