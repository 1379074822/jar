function page_nav(frm,num){
	frm.pageIndex.value = num;
	frm.submit();
}
$(".previousPage").on("click",function () {
    var obj = $(this);
    page_nav(document.forms[0],obj.attr("currentPageNo"));
});
$(".nextPage").on("click",function () {
    var obj = $(this);
    page_nav(document.forms[0],obj.attr("currentPageNo"));
});
$(".lastPage").on("click",function () {
    var obj = $(this);
    page_nav(document.forms[0],obj.attr("currentPageNo"));
});

$(".previousPageAddress").on("click",function () {
    var obj = $(this);
    page_nav(document.forms[1],obj.attr("currentPageNo"));
});
$(".nextPageAddress").on("click",function () {
    var obj = $(this);
    page_nav(document.forms[1],obj.attr("currentPageNo"));
});
$(".lastPageAddress").on("click",function () {
    var obj = $(this);
    page_nav(document.forms[1],obj.attr("currentPageNo"));
});

$(".previousPagePoint").on("click",function () {
    var obj = $(this);
    page_nav(document.forms[2],obj.attr("currentPageNo"));
});
$(".nextPagePoint").on("click",function () {
    var obj = $(this);
    page_nav(document.forms[2],obj.attr("currentPageNo"));
});
$(".lastPagePoint").on("click",function () {
    var obj = $(this);
    page_nav(document.forms[2],obj.attr("currentPageNo"));
});

