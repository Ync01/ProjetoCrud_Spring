/* Aprimoramentos progressivos, sem dependências externas. */
(() => {
    const reducedMotion = window.matchMedia('(prefers-reduced-motion: reduce)');
    const stage = document.querySelector('[data-scroll-stage]');
    if (stage) {
        const frame = stage.querySelector('.preview-frame');
        let scheduled = false;
        const update = () => {
            scheduled = false;
            if (reducedMotion.matches || window.innerWidth < 761) {
                frame.style.setProperty('--tilt', '0deg');
                frame.style.setProperty('--scale', '1');
                return;
            }
            const rect = stage.getBoundingClientRect();
            if (rect.bottom < 0 || rect.top > window.innerHeight) return;
            const progress = Math.max(0, Math.min(1, (window.innerHeight - rect.top) / (window.innerHeight * .85)));
            frame.style.setProperty('--tilt', `${(1 - progress) * 14}deg`);
            frame.style.setProperty('--scale', String(.94 + progress * .06));
        };
        const schedule = () => { if (!scheduled) { scheduled = true; requestAnimationFrame(update); } };
        window.addEventListener('scroll', schedule, {passive: true});
        window.addEventListener('resize', schedule);
        reducedMotion.addEventListener('change', schedule);
        update();
    }
    document.querySelectorAll('[data-password-toggle]').forEach(button => {
        const input = document.getElementById(button.dataset.passwordToggle);
        if (!input) return;
        button.hidden = false;
        button.addEventListener('click', () => {
            const show = input.type === 'password';
            input.type = show ? 'text' : 'password';
            button.textContent = show ? 'Ocultar' : 'Mostrar';
            button.setAttribute('aria-pressed', String(show));
        });
    });
})();
