/* 마이페이지에서 > MY 예약내역 (myReservation) 페이지로 이동하는 플러스 버튼 */
function redirectToMyReservation() {
    // 나의 예약 내역 페이지 경로 설정
    const reservationPath = 'Reservation';

    // 나의 예약 내역 페이지로 이동
    window.location.href = reservationPath;
}
document.addEventListener('DOMContentLoaded', function () {
    // 버튼을 찾아서 버튼 클릭 이벤트를 추가
    const reservationButton = document.getElementById('reservationButton');
    if (reservationButton) {
        reservationButton.addEventListener('click', redirectToMyReservation);
    }
});


/*예매 취소 버튼 기능 구현 _*/
function cancelReservation(reservation_id) {
    console.log("Received reservation_id:", reservation_id);
    if (confirm("정말로 해당 예약을 취소하시겠습니까?")) {
        // "예" 버튼 클릭 시 수행되는 기능
        fetch("/my/Reservation/" + reservation_id, {
            method: "DELETE",
            headers: {
                "Content-Type": "application/json"
            }
        }).then(response => {
            if (response.ok) {
                alert("예약이 성공적으로 취소되었습니다.");
                // 페이지 새로고침 및 취소된 예약 목록 갱신 코드
                window.location.reload();
            } else {
                alert("예약 취소에 실패했습니다.");
            }
        }).catch(error => {
            console.error("Error occurred:", error);
            alert("예약 취소에 실패하였습니다.");
        });
    } else {
        // 아니오 버튼 클릭 시 팝업창 나옴
        alert("예약 취소를 취소하였습니다.");
    }
}
/*이미 html 예매취소 버튼에 onclick 속성을 추가했기 때문에 addEventListener 함수 추가 안해도 됨*/


