function duplicate(selector) {
    var e = $(selector);
    var c = e.clone();
    c.find("input").attr("name", "file" + duplicate.count++);
    c.insertAfter(e).show();
}
duplicate.count = 1;

