function checkVersus() {
    const l1 = $('#l1-select').val()
    const l2 = $('#l2-select').val()

    if (l1 === l2) {
        alert('Please select two different languages')
        return
    }

    if (l1 && l2) {
        let url;
        if (l1.localeCompare(l2) < 0) {
            url = `${l1}-vs-${l2}`
        } else {
            url = `${l2}-vs-${l1}`
        }

        const path = window.location.pathname

        const platform = path.split('/')[1]
        const benchmark = path.split('/')[3]

        window.location.pathname = `/${platform}/versus/${benchmark}/${url}`
    }
}