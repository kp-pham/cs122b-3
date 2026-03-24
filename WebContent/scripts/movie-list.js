function handleResult(resultData) {
    let movieTable = jQuery("#movie-table-body");

    resultData.forEach(movie => {
        let row = `
            <tr>
                <td>
                    <a href="single-movie.html?id=${movie['id']}">${movie['title']}</a>
                </td>
                <td>${movie['year']}</td>
                <td>${movie['director']}</td>
                <td>${movie['genres'].slice(0, 3).join(', ')}</td>
                <td>
                    ${movie['stars'].slice(0, 3).map(({id, name}) => {
                        return `<a href="single-star.html?id=${id}">${name}</a>`
                    }).join(', ')}
                </td>
                <td>${movie['rating']}</td>
            </tr>
        `;

        movieTable.append(row);
    });
}

jQuery.ajax({
   dataType: "json",
   method: "GET",
   url: "api/",
   success: (resultData) => handleResult(resultData)
});