package whackAmole.main;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;

public class ScorePanel extends JPanel {
	// 스코어 패널
	// 윈도우의 북쪽에 붙을 패널. 스코어와 시간 등을 보여줄 것이다.
	int p_height = 200;
	
	JPanel p_west; // 왼쪽 패널
	JPanel p_center; // 센터 패널
	JPanel p_center_north; // 센터 북쪽 (목숨)
	JPanel p_center_south; // 센터 남쪽 (시간 , 점수)
	
	JButton bt_gameStart; // 게임 스타트 버튼
	//JButton bt_pause; // 중지 버튼
	JButton bt_setLevel; // 두더지 설정 버튼
	JButton bt_setSpeed; // 속도 설정 버튼
	JButton bt_gameSet; // 게임 종료 버튼
	
	JLabel score_field; // 점수 적히는 곳
	JLabel time_field; // 시간 적히는 곳
	JLabel life_field; // 목숨 적히는 곳
	
	int score = 0; // 점수
	int lifeCount = 5; // 목숨 갯수
	List<Life> lifes = new ArrayList<Life>(); // 목숨 담을 리스트
	
	Font font = new Font("돋움", Font.BOLD, 30); // 시계와 점수판 폰트
	Font font2 = new Font("바탕", Font.BOLD, 15); // 버튼들 폰트
	
	public ScorePanel() {
		/* ------------------------ 스코어 패널 기본 세팅 ------------------------ */
		this.setPreferredSize(new Dimension(0, p_height));
		this.setBackground(Color.RED);
		this.setLayout(new BorderLayout());
		
		/* ------------------------ 컴포넌트 생성 ------------------------ */
		// 센터 영역
		p_center = new JPanel();
		p_center_north = new JPanel();
		p_center_south = new JPanel();
		score_field = new JLabel("점수 : 0");
		time_field = new JLabel("0 : 0 : 0");
		life_field = new JLabel("목숨 : 0 0 0 0 0");
		// 서쪽 영역 
		p_west = new JPanel();
		bt_gameStart = new JButton(" 게임 시작 ");
		//bt_pause = new JButton(" 일시 중지 ");
		bt_setLevel = new JButton(" 두더지 설정 ");
		bt_setSpeed = new JButton(" 속도 설정 ");
		bt_gameSet = new JButton(" 게임 종료 ");
		
		
		/* ------------------------ 컴포넌트 세팅 ------------------------ */
		// 센터
		p_center.setBackground(Color.cyan);
		p_center.setLayout(new GridLayout(2, 1));
		p_center_north.setLayout(new FlowLayout());
		p_center_south.setLayout(new GridLayout(1, 1));
		p_center_north.setBackground(Color.WHITE);
		p_center_south.setBackground(Color.WHITE);
		life_field.setHorizontalAlignment(JLabel.CENTER);		// 가운데로 정렬
		time_field.setHorizontalAlignment(JLabel.CENTER);		// 가운데로 정렬
		score_field.setHorizontalAlignment(JLabel.CENTER);	// 가운데로 정렬
		life_field.setFont(font);		// 폰트 설정
		time_field.setFont(font);		// 폰트 설정
		score_field.setFont(font);	// 폰트 설정

		// 서쪽
		//p_west.setBackground(Color.LIGHT_GRAY);
		p_west.setLayout(new GridLayout(4, 1));
		bt_gameStart.setBackground(Color.WHITE);		// 버튼 배경색 설정
		bt_setLevel.setBackground(Color.WHITE);			// 버튼 배경색 설정
		bt_setSpeed.setBackground(Color.WHITE);		// 버튼 배경색 설정
		bt_gameSet.setBackground(Color.WHITE);			// 버튼 배경색 설정
		bt_gameStart.setFont(font2);		// 폰트 설정
		bt_setLevel.setFont(font2);			// 폰트 설정
		bt_setSpeed.setFont(font2);		// 폰트 설정
		bt_gameSet.setFont(font2);		// 폰트 설정
		
		
		/* ------------------------ 조립 ------------------------ */
		//p_center_north.add(life_field);
		p_center_south.add(time_field);
		p_center_south.add(score_field);
		createLife(); // 목숨 생성 및 조립
		p_center.add(p_center_north);
		p_center.add(p_center_south);
		
		p_west.add(bt_gameStart);
		//p_west.add(bt_pause);
		p_west.add(bt_setLevel);
		p_west.add(bt_setSpeed);
		p_west.add(bt_gameSet);
		
		
		this.add(p_center, BorderLayout.CENTER);
		this.add(p_west, BorderLayout.WEST);
	
	}
	
	public void createLife() { // 목숨 다시 만드는 메서드
		for(int i=0; i<lifeCount; i++) {
			Life life = new Life();
			p_center_north.add(life);
			lifes.add(life); // 목숨 리스트에도 넣어주자
		}
	}
	
	public void deleteLife() { // 목숨 다 지우는 메서드
		for(int i=0; i<lifes.size(); i++) {
			this.p_center_north.remove(lifes.get(i));
		}
		lifes.clear();
	}
	
	public void deleteOneLife() {
		// 0번을 지워도 계속 0번으로 남은 객체들이 밀리기 때문에 0번을 계속 지우다보면 0이 될것이다.
		this.p_center_north.remove(lifes.get(0));
		lifes.remove(0);
	}

}
