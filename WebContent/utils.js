const navPlaceholder = $("#nav-placeholder");
const logoutForm = $("#logout");

const baseURL = window.location.origin + '/' + window.location.pathname.split('/')[1];

function handleLogoutFormSubmit(formSubmitEvent) {
    formSubmitEvent.preventDefault();

    $.ajax({
        url: baseURL + "/api/logout",
        method: "POST"
    });
}

navPlaceholder.load("nav.html");
logoutForm.submit(handleLogoutFormSubmit);
