$("#eBtn").click(function(){
    const boardNum = $("#boardNum").val();
    if(confirm('해당 글을 수정 하시겠습니까?')) {
        window.location.href = "/board/noticeEdit?boardNum=" + boardNum;
    } else {
        window.location.href = "/board/noticeDetail?boardNum=" + boardNum;
    }
});

const sBtn = document.getElementById("sBtn");

const Form = document.getElementById("form");

sBtn.addEventListener('click', function(){
    if(confirm('해당 내용으로 글을 수정하시겠습니까?')){
        const boardNum = document.getElementById("boardNum");
        const xhr = new XMLHttpRequest();
        const formData = new FormData();
        formData.append('boardWriter', Form['boardWriter'].value);
        formData.append('boardValue', Form['boardValue'].value);
        formData.append('boardTitle', Form['boardTitle'].value);
        formData.append('boardContent', Form['boardContent'].value);
        formData.append('boardNum', Form['boardNum'].value);
        xhr.open('post', '/board/noticeEdit');
        xhr.onreadystatechange = () => {
            if (xhr.readyState === XMLHttpRequest.DONE) {
                if (xhr.status >= 200 && xhr.status < 300) {
                    alert("글 수정이 완료 되었습니다.");
                    window.location.href = "/board/noticeDetail?boardNum=" + Form['boardNum'].value;
                } else {
                    alert("서버와의 연결이 끊어졌습니다.")
                }
            }
        };
        xhr.send(formData);
    } else {
        return false;
    }
});
