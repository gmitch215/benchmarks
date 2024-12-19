$(document).ready(function() {
    const searchInput = $('#benchmark-search');

    searchInput.on('input', function() {
        const searchQuery = DOMPurify.sanitize(searchInput.val().trim()).toLowerCase();

        $('.benchmark-parent > div').each(function() {
            const div = $(this);

            const id = div.attr('id') || ''
            const className = div.attr('class') || ''
            const title = div.attr('title') || ''

            const matches = id.toLowerCase().includes(searchQuery) ||
                className.toLowerCase().includes(searchQuery) ||
                title.toLowerCase().includes(searchQuery)

            if (matches) {
                div.show()
            } else {
                div.hide()
            }
        })
    })
})