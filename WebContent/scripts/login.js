let login_form = $("#login_form");

function handleLoginResult(resultDataString) {
    let resultDataJson = JSON.parse(resultDataString);

    if (resultDataJson["status"] === "success") {
        window.location.replace("index.html");
    } else {
        $("#login_error_message").text(resultDataJson["message"]);
    }
}

function submitLoginForm(formSubmitEvent) {
    formSubmitEvent.preventDefault();

    $.ajax(
        "api/login", {
            method: "POST",
            data: login_form.serialize(),
            success: handleLoginResult
        }
    );
}

login_form.submit(submitLoginForm);