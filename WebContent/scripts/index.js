function handleClick(clickEvent) {
    clickEvent.preventDefault();

    const type = $(clickEvent.currentTarget).data("type");
    const value = $(clickEvent.currentTarget).data("value");

    sessionStorage.setItem("movieListState", JSON.stringify({
        type: "browse",
        [type]: value,
        page: 1,
        sort: "title-asc-rating-desc",
        itemsPerPage: 25
    }));

    window.location.href = `list.html?${encodeURIComponent(type)}=${encodeURIComponent(value)}`;
}

function handleResult(resultData) {
    let genresList = $("#genres-list");

    resultData.forEach((genre) => {
        let link = $(`<a href="list.html?genre=${encodeURIComponent(genre)}" 
                              data-type="genre" data-value="${genre}" class="text-center">${genre}</a>`);
        link.on("click", handleClick);
        genresList.append(link);
    });
}

function showPrefixes() {
    let prefixList = $("#prefix-list");
    let prefixes = "ABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789*";

    for (const prefix of prefixes) {
        let link = $(`<a href="list.html?prefix=${encodeURIComponent(prefix)}" 
                              data-type="prefix" data-value="${prefix}">${prefix}</a>`);
        link.on("click", handleClick);
        prefixList.append(link);
    }
}

function submitSearchForm(formSubmitEvent) {
    formSubmitEvent.preventDefault();

    let title = $.trim($("input[name=title]").val());
    let year = $.trim($("input[name=year]").val());
    let director = $.trim($("input[name=director]").val());
    let star = $.trim($("input[name=star]").val());

    if (title.length === 0 && year.length === 0 && director.length === 0 && star.length === 0) {
        return;
    }

    let params = [];

    if (title.length !== 0) {
        params.push(`title=${encodeURIComponent(title)}`);
    }

    if (year.length !== 0) {
        params.push(`year=${encodeURIComponent(year)}`);
    }

    if (director.length !== 0) {
        params.push(`director=${encodeURIComponent(director)}`);
    }

    if (star.length !== 0) {
        params.push(`star=${encodeURIComponent(star)}`);
    }

    window.location.href = "search.html?" + params.join("&");
}

showPrefixes();

jQuery.ajax({
    dataType: "json",
    method: "GET",
    url: "api/genres",
    success: (resultData) => handleResult(resultData)
});

$("#search-form").submit(submitSearchForm);