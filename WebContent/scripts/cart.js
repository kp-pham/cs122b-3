function handleResult(resultData) {
    let itemsList = $("#items-table-body");

    resultData["items"].forEach(item => {
        let row = `
            <tr>
                <td>
                    <a href="single-movie.html?id=${item['id']}">${item['title']}</a>
                </td>
                <td>
                    <form id="quantityForm" method="POST" action="#">
                        <button class="rounded text-white bg-dark">-</button>
                        ${item['quantity']}
                        <button class="rounded text-white bg-dark">+</button>
                    </form>
                </td>
                <td>
                    <form id="removeForm" method="POST" action="#">
                        <button class="rounded text-white bg-danger border-danger">Remove</button>
                    </form>
                </td>
                <td>${item['price']}</td>
                <td>${item['subtotal']}</td>
            </tr>
        `;

        itemsList.append(row);
    });

    let itemsTable = $("#items-table");

    let footer = `
        <tfoot>
            <tr>
                <td colspan="3"></td>
                <td class="text-end text-dark fw-bold">Total:</td>
                <td>${resultData["total"]}</td>
            </tr>
        </tfoot>
    `;

    itemsTable.append(footer);
}

jQuery.ajax({
    dataType: "json",
    method: "GET",
    url: "api/cart",
    success: (resultData) => handleResult(resultData)
});