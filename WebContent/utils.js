const baseURL = window.location.origin + '/' + window.location.pathname.split('/')[1];

const navPlaceholder = $("#nav-placeholder");
navPlaceholder.load("nav.html", () => {
    const logoutForm = $("#logout-form");


    function submitLogoutForm(formSubmitEvent) {
        formSubmitEvent.preventDefault();

        $.ajax({
            url: baseURL + "/api/employees/logout",
            method: "POST",
        });
    }

    logoutForm.submit(submitLogoutForm);
});
