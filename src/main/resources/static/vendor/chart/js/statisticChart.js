// 서버에서 데이터 받아오기
fetch('http:localhost:8080/admin/statistic/gender')
    .then(response => response.json())
    .then(data => {
        // 남성과 여성 데이터 분리
        const labels = data.map(item => item.gender);
        const counts = data.map(item => item.count);

        // Chart.js 차트 생성
        const ctx = document.querySelector('#myChart');
        const myChart = new Chart(ctx, {
            type: 'doughnut',
            data: {
                labels: labels,
                datasets: [{
                    label: '남녀 성비',
                    data: counts,
                    borderWidth: 0,
                    backgroundColor: ['#42A5F5', '#FF7043']
                }]
            },
            options: {
                responsive: true,
                plugins: {
                    legend: {
                        position: 'top',
                    },
                    tooltip: {
                        callbacks: {
                            label: function(context) {
                                const percentage = data[context.dataIndex].percentage;
                                return `${context.label}: ${context.raw}명 (${percentage}%)`;
                            }
                        }
                    }
                }
            }
        });
    })
    .catch(error => console.error('Error fetching gender ratio data:', error));
