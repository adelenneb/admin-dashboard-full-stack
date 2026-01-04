import { Component } from '@angular/core';
import { ChartConfiguration, ChartData, ChartType } from 'chart.js';

interface StatCard {
  title: string;
  value: string;
  icon: string;
}

@Component({
  selector: 'app-home',
  templateUrl: './home.component.html',
  styleUrls: ['./home.component.css']
})
export class HomeComponent {
  stats: StatCard[] = [
    { title: 'Active Users', value: '1,248', icon: 'group' },
    { title: 'Revenue', value: '$82,300', icon: 'trending_up' },
    { title: 'Tickets', value: '56 open', icon: 'support_agent' }
  ];

  barChartOptions: ChartConfiguration['options'] = {
    responsive: true,
    plugins: {
      legend: { display: true, position: 'bottom' }
    },
    scales: {
      x: {},
      y: { beginAtZero: true }
    }
  };

  barChartType: ChartType = 'bar';

  barChartData: ChartData<'bar'> = {
    labels: ['Jan', 'Feb', 'Mar', 'Apr', 'May', 'Jun'],
    datasets: [
      {
        data: [120, 150, 180, 220, 200, 260],
        label: 'New Users',
        backgroundColor: '#2563eb'
      },
      {
        data: [80, 90, 110, 130, 140, 170],
        label: 'Active Sessions',
        backgroundColor: '#10b981'
      }
    ]
  };
}
