const loginBtn = document.getElementById("loginBtn");

loginBtn.addEventListener('click', function(){
    const Form = document.getElementById("form");

    if($("#memberEmail").val()=="") {
        alert("아이디를 입력하세요.");
        $("#memberEmail").focus();
        return false;
    }
    if($("#memberPassword").val()=="") {
        alert("비밀번호를 입력하세요.");
        $("#memberPassword").focus();
        return false;
    }

    const xhr = new XMLHttpRequest();
    const formData = new FormData();
    formData.append("memberEmail", Form['memberEmail'].value);
    formData.append("memberPassword", Form['memberPassword'].value);
    // formdata를 append 시 JSON 방식으로 controller로 데이터를 넘겨주는 내장 객체
    xhr.open("post", "/member/login");
    xhr.onreadystatechange = () => {
        if(xhr.readyState === XMLHttpRequest.DONE) {
            if(xhr.status >= 200 && xhr.status < 300) {
                const responseJson = JSON.parse(xhr.responseText);
                if(responseJson["result"] == "success") {
                    alert("로그인에 성공하셨습니다.")
                    window.location.href = "/"
                } else {
                    alert("아이디와 비밀번호를 다시 확인해 주세요.")
                    window.location.href = "/member/login"
                }

            } else {
                alert("실패")
                window.location.href = "/member/login"
            }
        }
    };
    xhr.send(formData);
})