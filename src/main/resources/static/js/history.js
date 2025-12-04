$(document).ready(function() {
    const modal = $('#details-modal');
    const closeModalBtn = modal.find('.close-btn');

    // 오늘 날짜를 '실행 일자' 필드의 기본값으로 설정
    const today = new Date().toISOString().split('T')[0];
    $('#searchExecutionDate').val(today);

    // 페이지 로드 시 오늘 날짜 기준으로 기록 로드
    loadHistory();

    // 검색 버튼 클릭
    $('#search-btn').on('click', function() {
        loadHistory();
    });

    // 상세 보기 버튼 클릭 이벤트
    $('#history-table tbody').on('click', '.details-btn', function() {
        const executionId = $(this).data('execution-id');
        loadDetails(executionId);
    });

    // 모달 닫기
    closeModalBtn.on('click', () => modal.hide());
    $(window).on('click', (e) => {
        if (e.target === modal[0]) {
            modal.hide();
        }
    });
});

function loadHistory() {
    const searchParams = {
        jobName: $('#searchJobName').val(),
        executionDate: $('#searchExecutionDate').val(),
        status: $('#searchStatus').val()
    };

    $.ajax({
        url: '/api/history/jobs',
        type: 'GET',
        data: searchParams,
        success: function(history) {
            const tableBody = $('#history-table tbody');
            tableBody.empty();
            history.forEach(function(exec) {
                const row = `<tr>
                                <td>${exec.id}</td>
                                <td>${exec.jobName}</td>
                                <td>${formatDateTime(exec.startTime)}</td>
                                <td>${formatDateTime(exec.endTime)}</td>
                                <td><span class="status status-${exec.status.toLowerCase()}">${exec.status}</span></td>
                                <td>
                                    <button class="action-btn details-btn" data-execution-id="${exec.id}">상세 보기</button>
                                </td>
                             </tr>`;
                tableBody.append(row);
            });
        },
        error: function(xhr) {
            alert('기록을 불러오는 데 실패했습니다: ' + xhr.responseText);
        }
    });
}

function loadDetails(executionId) {
    $.ajax({
        url: `/api/history/jobs/${executionId}`,
        type: 'GET',
        success: function(details) {
            const contentDiv = $('#details-content');
            contentDiv.empty();
            const detailsHtml = `
                <p><strong>실행 ID:</strong> ${details.id}</p>
                <p><strong>Job 이름:</strong> ${details.jobName}</p>
                <p><strong>시작 시간:</strong> ${formatDateTime(details.startTime)}</p>
                <p><strong>종료 시간:</strong> ${formatDateTime(details.endTime)}</p>
                <p><strong>상태:</strong> <span class="status status-${details.status.toLowerCase()}">${details.status}</span></p>
                <p><strong>파라미터:</strong></p>
                <pre>${details.jobParameters}</pre>
                ${details.stackTrace ? `
                <p><strong>스택 트레이스:</strong></p>
                <pre>${details.stackTrace}</pre>
                ` : ''}
            `;
            contentDiv.html(detailsHtml);
            $('#details-modal').css('display', 'flex');
        },
        error: function(xhr) {
            alert('상세 정보를 불러오는 데 실패했습니다: ' + xhr.responseText);
        }
    });
}

function formatDateTime(dateTimeString) {
    if (!dateTimeString) return 'N/A';
    const date = new Date(dateTimeString);
    return date.toLocaleString('ko-KR', {
        year: 'numeric', month: '2-digit', day: '2-digit',
        hour: '2-digit', minute: '2-digit', second: '2-digit',
        hour12: false
    }).replace(/\. /g, '.');
}
