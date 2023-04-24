const form = document.getElementById("qForm");
const qBtn = document.getElementById("qBtn");

qBtn.addEventListener("click", function(){
    if(confirm("정말 탈퇴하시겠습니까?")) {
        const memberEmail = form['memberEmail'].value;
        const memberPassword = form['memberPassword'].value;
        $.ajax({
            url: "/member/deleteUser",
            method: "post",
            data: {"memberEmail" : memberEmail, "memberPassword" : memberPassword},
            success: function() {
                alert("회원 탈퇴 되었습니다.");
                window.location.href="/member/logout";
            }
        });
    } else {
        return false;
    }
});


// -------------------------------------- 회원 정보 수정
const btn = document.getElementById("modifyBtn");

// 등록 클릭시 발생하는 이벤트
btn.addEventListener('click', function () {


    if($("#memberName").val()=="") {
        alert("이름을 입력하세요.");
        $("#memberName").focus();
        return false;
    }
    if($("#memberPassword").val()=="") {
        alert("비밀번호를 입력하세요.");
        $("#memberPassword").focus();
        return false;
    }
    if($("#memberPasswordCheck").val()=="") {
        alert("비밀번호 확인을 진행해주세요");
        $("#memberPasswordCheck").focus();
        return false;
    }
    if($("#memberPasswordCheck").val() != $("#memberPassword").val()) {
        alert("비밀번호 확인가 일치하지 않습니다.");
        $("#memberPasswordCheck").focus();
        return false;
    }

    const Form = document.getElementById("form");

    const xhr = new XMLHttpRequest();
    const formData = new FormData();
    formData.append('memberName', Form['memberName'].value);
    formData.append('memberPass', Form['memberPassword'].value);
    formData.append('memberNum', Form['memberNum'].value);

    xhr.open('post', '/member/modify');
    xhr.onreadystatechange = () => {
        if (xhr.readyState === XMLHttpRequest.DONE) {
            if (xhr.status >= 200 && xhr.status < 300) {
                const responseJSON = JSON.parse(xhr.responseText);
                if(responseJSON['result'] == "success") {
                    alert("자동적으로 로그아웃 됩니다. 다시 로그인 해주세요.");
                    window.location.href="/member/logout";
                } else {
                    alert("실패");
                    window.location.href="/member/mypage";
                }
            } else {
                alert("실패");
                window.location.href="/member/mypage";
            }
        }
    };
    xhr.send(formData);
});
