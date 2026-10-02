(() => {
    const tabs = Array.from(document.querySelectorAll('[role="tab"]'));
    const activate = (name, focus = false) => {
        tabs.forEach(tab => {
            const active = tab.dataset.tab === name;
            tab.classList.toggle('active', active);
            tab.setAttribute('aria-selected', String(active));
            tab.tabIndex = active ? 0 : -1;
            if (active && focus) tab.focus();
        });
        document.querySelectorAll('.tab-panel').forEach(panel => panel.classList.toggle('active', panel.id === 'panel-' + name));
    };
    if (tabs.length) {
        document.body.classList.add('js-tabs');
        activate(location.hash === '#historico' ? 'saida' : 'entrada');
        tabs.forEach((tab, index) => {
            tab.addEventListener('click', () => {
                activate(tab.dataset.tab);
                history.replaceState(null, '', tab.dataset.tab === 'saida' ? '#historico' : location.pathname + location.search);
            });
            tab.addEventListener('keydown', event => {
                let next;
                if (event.key === 'ArrowRight') next = (index + 1) % tabs.length;
                if (event.key === 'ArrowLeft') next = (index + tabs.length - 1) % tabs.length;
                if (event.key === 'Home') next = 0;
                if (event.key === 'End') next = tabs.length - 1;
                if (next !== undefined) { event.preventDefault(); tabs[next].click(); tabs[next].focus(); }
            });
        });
    }
    const normalize = value => value.toUpperCase().replace(/[^A-Z0-9]/g, '');
    document.querySelectorAll('[data-filter-table]').forEach(input => {
        const table = document.getElementById(input.dataset.filterTable);
        if (!table) return;
        const feedback = document.createElement('p');
        feedback.className = 'empty filter-empty';
        feedback.setAttribute('role', 'status');
        feedback.hidden = true;
        table.closest('.table-scroll').after(feedback);
        const filter = () => {
            const term = normalize(input.value);
            let visible = 0;
            table.querySelectorAll('tbody tr').forEach(row => {
                const matches = normalize(row.cells[0].textContent).includes(term);
                row.classList.toggle('hidden-row', !matches);
                if (matches) visible++;
            });
            feedback.hidden = visible > 0;
            feedback.textContent = visible ? '' : 'Nenhum registro corresponde a esta placa.';
        };
        input.addEventListener('input', filter);
        filter();
        input.closest('form').addEventListener('submit', event => { event.preventDefault(); filter(); });
    });
    const plate = document.getElementById('placa');
    if (plate) plate.addEventListener('input', () => {
        const start = plate.selectionStart;
        plate.value = plate.value.toUpperCase();
        plate.setSelectionRange(start, start);
    });
})();
