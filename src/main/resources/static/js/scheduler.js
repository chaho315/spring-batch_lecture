$(document).ready(function() {
    const modal = $('#edit-modal');
    const closeModalBtn = modal.find('.close-btn');

    // 페이지 로드 시 초기 데이터 로드
    loadJobs();
    loadJobNames();

    // 검색 버튼 클릭
    $('#search-btn').on('click', function() {
        loadJobs();
    });

    // 신규 스케줄 등록
    $('#add-schedule-form').on('submit', function(e) {
        e.preventDefault();
        const jobName = $('#jobName').val();
        const cronExpression = $('#cronExpression').val();
        if (!jobName) { alert("Job을 선택해주세요."); return; }

        $.ajax({
            url: '/api/scheduler/jobs',
            type: 'POST',
            contentType: 'application/json',
            data: JSON.stringify({ jobName: jobName, cronExpression: cronExpression }),
            success: (response) => {
                showFeedback(response, true);
                loadJobs();
                $('#add-schedule-form')[0].reset();
            },
            error: (xhr) => showFeedback(xhr.responseText, false)
        });
    });

    // 스케줄 목록 테이블의 이벤트 위임
    $('#job-table tbody').on('click', '.action-btn', function() {
        const jobName = $(this).data('job-name');
        const cron = $(this).data('cron');

        if ($(this).hasClass('edit-btn')) {
            $('#modalJobName').text(jobName);
            $('#editJobName').val(jobName);
            $('#editCronExpression').val(cron);
            modal.css('display', 'flex');
        } else if ($(this).hasClass('delete-btn')) {
            if (confirm(`'${jobName}' 스케줄을 정말 삭제하시겠습니까?`)) {
                $.ajax({
                    url: `/api/scheduler/jobs/${jobName}`,
                    type: 'DELETE',
                    success: (response) => {
                        showFeedback(response, true);
                        loadJobs();
                    },
                    error: (xhr) => showFeedback(xhr.responseText, false)
                });
            }
        } else if ($(this).hasClass('trigger-btn')) {
            if (confirm(`'${jobName}' Job을 지금 바로 실행하시겠습니까?`)) {
                $.ajax({
                    url: `/api/scheduler/jobs/${jobName}/trigger`,
                    type: 'POST',
                    success: (response) => showFeedback(response, true),
                    error: (xhr) => showFeedback(xhr.responseText, false)
                });
            }
        }
    });

    // 수정 모달 관련 이벤트
    closeModalBtn.on('click', () => modal.hide());
    $(window).on('click', (e) => {
        if (e.target === modal[0]) {
            modal.hide();
        }
    });
    $('#edit-schedule-form').on('submit', function(e) {
        e.preventDefault();
        const jobName = $('#editJobName').val();
        const cronExpression = $('#editCronExpression').val();

        $.ajax({
            url: `/api/scheduler/jobs/${jobName}`,
            type: 'PUT',
            contentType: 'application/json',
            data: JSON.stringify({ cronExpression: cronExpression }),
            success: (response) => {
                showFeedback(response, true);
                modal.hide();
                loadJobs();
            },
            error: (xhr) => showFeedback(xhr.responseText, false)
        });
    });
});

function loadJobNames() {
    $.ajax({
        url: '/api/scheduler/job-names',
        type: 'GET',
        success: (jobNames) => {
            const select = $('#jobName');
            select.empty().append('<option value="">-- Job을 선택하세요 --</option>');
            jobNames.forEach((name) => select.append(`<option value="${name}">${name}</option>`));
        },
        error: (xhr) => showFeedback('Job 이름 목록을 불러오는 데 실패했습니다: ' + xhr.responseText, false)
    });
}

function loadJobs() {
    const searchJobName = $('#searchJobName').val();
    $.ajax({
        url: '/api/scheduler/jobs',
        type: 'GET',
        data: { jobName: searchJobName },
        success: (jobs) => {
            const tableBody = $('#job-table tbody');
            tableBody.empty();
            jobs.forEach((job) => {
                const row = `<tr>
                                <td>${job.jobName}</td>
                                <td>${job.cronExpression}</td>
                                <td>
                                    <button class="action-btn trigger-btn" data-job-name="${job.jobName}">실행</button>
                                </td>
                                <td>
                                    <button class="action-btn edit-btn" data-job-name="${job.jobName}" data-cron="${job.cronExpression}">수정</button>
                                    <button class="action-btn delete-btn" data-job-name="${job.jobName}">삭제</button>
                                </td>
                             </tr>`;
                tableBody.append(row);
            });
        },
        error: (xhr) => showFeedback('스케줄 목록을 불러오는 데 실패했습니다: ' + xhr.responseText, false)
    });
}

function showFeedback(message, isSuccess) {
    const feedbackDiv = $('#feedback');
    feedbackDiv.text(message).removeClass('feedback-success feedback-error').addClass(isSuccess ? 'feedback-success' : 'feedback-error');
    feedbackDiv.fadeIn().delay(3000).fadeOut();
}
