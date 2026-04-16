const starsForm = $("#stars-form");

const baseURL = window.location.origin + '/' + window.location.pathname.split('/')[1];

function handleSuccess(resultData) {

}

function handleFailure(jqXHR) {

}

function handleFormSubmit(formSubmitEvent) {
    formSubmitEvent.preventDefault();

    jQuery.ajax({
        url: baseURL + "/api/employees/star",
        success:handleSuccess,
        error: (jqXHR) => handleFailure(jqXHR)
    });
}

starsForm.submit(handleFormSubmit);