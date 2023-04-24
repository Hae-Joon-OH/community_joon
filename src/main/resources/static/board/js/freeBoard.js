window.onload = first();
function first() {
    const boardLikesUser = $("#nName").val();
    const boardNum = $("#bNum").val();
    const likeCount = $("#likeCount");
    console.log(boardLikesUser);
    console.log(boardNum);
    $.ajax({
        url : "/board/SelectLike",
        method : "get",
        data : {"boardLikesUser" : boardLikesUser, "boardNum" : boardNum},
        success: function (responseJson) {
            responseJson = JSON.parse(responseJson);

            likeCount.empty().append(responseJson['num']);
            // empty()를 써서 계속 뒤에 붙는게 아니라, 그 전걸 비우고 새로 append하는 형식으로 설정.

            if(responseJson['result'] == "LikedIt") {
                $("#lBtn").addClass("invisible");
                $("#dlBtn").removeClass("invisible");
                $("#dlBtn").addClass("visible");
            } else {
                $("#dlBtn").addClass("invisible");
                $("#lBtn").removeClass("invisible");
                $("#lBtn").addClass("visible");
            }
        }
    })
}

const lBtn = document.getElementById("lBtn");
lBtn.addEventListener("click", function(){
    const boardWriter = $("#nName").val();
    const boardNum = $("#bNum").val();
    $.ajax({
        url : "/board/like",
        method : "get",
        data : {"boardWriter" : boardWriter, "boardNum" : boardNum},
        success : function (responseJson) {
            responseJson = JSON.parse(responseJson);
            if(responseJson['result'] == 1) {
                alert("좋아요 되었습니다.")
                first();
            } else {
                alert("자신의 글에 좋아요 할 수 없습니다.");
            }
        },
        error : function() {
            alert("오류가 발생하였습니다.");
        }
    })
})

const dlBtn = document.getElementById("dlBtn");
dlBtn.addEventListener("click", function(){
    const boardWriter = $("#nName").val();
    const boardNum = $("#bNum").val();
    $.ajax({
        url : "/board/undolike",
        method : "get",
        data : {"boardWriter" : boardWriter, "boardNum" : boardNum},
        success : function (responseJson) {
            responseJson = JSON.parse(responseJson);
            if(responseJson['result'] == 1) {
                alert("좋아요 취소 되었습니다.")
                first();
            } else {
                alert("오류입니다.");
            }
        },
        error : function() {
            alert("오류가 발생하였습니다.");
        }
    })
})

$("#deleteBtn").click(function(){
    const boardNum = $("#boardNum").val();
    if(confirm('정말 삭제하시겠습니까?')) {
        window.location.href = "/board/deletePost?boardNum=" + boardNum;
    } else {
        window.location.href = "/board/freeDetail?boardNum=" + boardNum;
    }
});

$("#editBtn").click(function(){
    const boardNum = $("#boardNum").val();
    if(confirm('해당 글을 수정 하시겠습니까?')) {
        window.location.href = "/board/editPost?boardNum=" + boardNum;
    } else {
        window.location.href = "/board/freeDetail?boardNum=" + boardNum;
    }
});



// ------------------------------------------ 댓글 시작
const commentBtn = document.getElementById("commentBtn");
commentBtn.addEventListener('click', function () {
    const boardNum = $("#commentBNum").val();
    const commentContent = $("#comment").val();
    const commentWriter = $("#commentWriter").val();

    if(commentContent == ""){
        alert("댓글 내용을 입력하세요!")
        return false;
    }

    console.log(boardNum, commentContent, commentWriter)
    $.ajax({
        url: "/board/insertComment",
        method: "GET",
        data: {"commentContent": commentContent, "commentWriter": commentWriter, "boardNum": boardNum},
        success: function (responseJson) {
            responseJson = JSON.parse(responseJson)
            if (responseJson['result'] == 1) {
                alert("댓글이 게시 되었습니다.")
                window.location.href = "/board/freeDetail?boardNum=" + boardNum;
            } else {
                alert("오류입니다,")
            }
        }
    })
})

$('.replyForm').hide();
$(document).ready(function () {
    $('.replyBtn').each(function (index) {
        $(this).addClass('replyBtn' + index);
        $('.replyBtn' + index).click(function () {
            $('.replyForm' + index).show();
        })
    })
    $('.replyForm').each(function (index) {
        $(this).addClass('replyForm' + index);
    })
    $('.replyTextarea').each(function (index) {
        $(this).addClass('replyTextarea' + index);
    })
    $('.replySendBtn').each(function (index) {
        $(this).addClass('replySendBtn' + index);
    })
})
