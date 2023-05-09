const form = document.getElementById("form");
const sBtn = document.getElementById("sBtn");

sBtn.addEventListener('click', function(){
    const boardWriter = $("#boardWriter").val();
    const boardValue = $("#boardValue").val();
    const boardTitle = $("#boardTitle").val();
    const boardContent = $("#boardContent").val();

    $.ajax({
        url: "/board/noticeInsert",
        method: "post",
        data: {"boardWriter" : boardWriter, "boardValue" : boardValue, "boardTitle" : boardTitle, "boardContent" : boardContent},
        success: function(responseJson) {
            responseJson = JSON.parse(responseJson);
            alert("글 작성에 성공하였습니다.")
            // window.location.href="/board/noticeDetail?boardNum=" + responseJson['result'];
            window.location.href="/board/notice";
        },
        error: function () {
            alert("서버와 연결이 끊겼습니다.")
        }
    })
});