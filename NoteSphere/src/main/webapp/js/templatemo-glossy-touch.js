// =============================================
//  NoteSphere — Common JS
// =============================================

// ── Sidebar Toggle (Mobile Hamburger) ──
function toggleSidebar() {
    document.querySelector('.sidebar').classList.toggle('open');
    document.querySelector('.sidebar-overlay').classList.toggle('active');
    document.querySelector('.hamburger').classList.toggle('open');
}

// Close sidebar when a nav link is clicked on mobile
document.addEventListener('DOMContentLoaded', function () {
    const navLinks = document.querySelectorAll('.nav-links a:not([onclick])');
    navLinks.forEach(link => {
        link.addEventListener('click', function () {
            if (window.innerWidth <= 768) {
                document.querySelector('.sidebar').classList.remove('open');
                document.querySelector('.sidebar-overlay').classList.remove('active');
                document.querySelector('.hamburger').classList.remove('open');
            }
        });
    });
});

// ── Logout ──
function logout() {
    localStorage.removeItem('notesphere_user');
    sessionStorage.clear();
    window.location.href = 'login.html';
}

// ── Session Guard (protect pages) ──
// Uncomment below when login is ready:
// function checkSession() {
//     const user = JSON.parse(localStorage.getItem('notesphere_user'));
//     if (!user) window.location.href = 'login.html';
// }
// checkSession();
