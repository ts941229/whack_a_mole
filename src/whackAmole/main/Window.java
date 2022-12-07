package whackAmole.main;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Image;
import java.awt.Point;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.swing.JFrame;
import javax.swing.JMenuBar;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

public class Window extends JFrame implements ActionListener, Runnable{
	
	// 요소 선언
	JPanel p_center; // 센터 영역
	JPanel p_north; // 북쪽 영역
	GamePanel gamePanel; // 게임 패널
	ScorePanel scorePanel; // 유저에게 점수 및 시간 등을 보여줄 패널
	
	Thread gameStartThead; // 게임시작과 동시에 생성될 쓰레드 ( 시간 , 두더지 활동 시작 등 )
	
	static boolean isRunning = false; // 게임이 실행중인지 아닌지
	
	
	public Window() {
		
		/* ------------------------ 윈도우 기본 세팅 ------------------------ */
		this.setTitle("두더지 잡기 Game");
		this.setLayout(new BorderLayout()); // 레이아웃 설정
		this.setResizable(false);
		this.setPreferredSize(new Dimension(800, 800));
		
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); // 닫을시 프로그램 종료
		
		
		/* ------------------------ 컴포넌트 생성 ------------------------ */
		p_center = new JPanel(); // 센터영역 생성
		p_north = new JPanel(); // 북쪽영역 생성
		
		scorePanel = new ScorePanel(); // 스코어패널 생성
		gamePanel = new GamePanel(this, scorePanel); // 게임패널 생성
		
		
		/* ------------------------ 기본 컴포넌트 세팅 ------------------------ */
		// 센터 패널 세팅
		p_center.setLayout(new BorderLayout());
		
		// 북쪽 패널 세팅
		p_north.setLayout(new BorderLayout());
		// 리스너 조립
		scorePanel.bt_gameStart.addActionListener(this); // 게임시작 버튼에 액션리스너 부착
		scorePanel.bt_setLevel.addActionListener(this); // 두더지설정 버튼에 액션리스너 부착
		scorePanel.bt_setSpeed.addActionListener(this); // 속도설정 버튼에 액션리스너 부착
		scorePanel.bt_gameSet.addActionListener(this); // 게임종료 버튼에 액션리스너 부착
		
		// 메소드들을 윈도우 클래스에서 일괄적으로 관리해야할까 각 메서드들을 각 클래스 (ScorePanel , Mole , GamePanel 등)
		// 에서 각자 관리 해야할까..
		
		
		/* ------------------------ 조립 ------------------------ */
		this.add(p_center, BorderLayout.CENTER); // 게임패널 조립
		this.add(p_north, BorderLayout.NORTH); // 스코어패널 조립
		
		p_center.add(gamePanel, BorderLayout.CENTER);
		p_north.add(scorePanel, BorderLayout.CENTER);
		
		
		this.pack();
		this.setLocationRelativeTo(null);
		this.setVisible(true); // 윈도우창을 눈에 보이게 해주는 메서드
		customCursor(); // 커서 커스텀!
	}
	

	@Override
	public void actionPerformed(ActionEvent e) {
		
		/* -------------------------------- 게임시작 버튼 눌렀을 때 -------------------------------- */
		
		if(e.getSource()==scorePanel.bt_gameStart) { // 게임시작 버튼 누르면 읽음
			System.out.println("게임 시작 눌렸어요");
			// (게임을 시작하시겠습니까?)얼럿 창 띄우고 true 일시
			// 쓰레드 시작하면서 시간이 가고, 두더지들이 올라오기 시작한다
			// 모든 기능들이 게임 시작 눌렀을때 시작되어야 한다.
			// 일정 시간이 지나면 게임 종료
 			if(isRunning == false) {
				if(JOptionPane.showConfirmDialog(this, "게임을 시작 하시겠습니까?", "게임 시작", JOptionPane.YES_NO_OPTION) == 0) {
					scorePanel.score = 0;
					scorePanel.score_field.setText("점수 : " + scorePanel.score); // 게임 시작과 동시에 스코어 초기화!
					
					isRunning = true;
					gameStart(); // 게임 시작!
					gamePanel.startMoleThread();
					
					// 게임이 다시 시작될 때
					// 목숨이 남아있거나 할 수 있으니 다 없애고 다시 생성
					scorePanel.deleteLife();
					scorePanel.createLife();
				}
			}else {
				JOptionPane.showMessageDialog(this, "이미 게임이 실행중입니다.");
			}
		}
		/* -------------------------------- 게임시작 버튼 종료 -------------------------------- */
		
		
		/* -------------------------------- 게임종료 버튼 눌렀을 때 -------------------------------- */
		if(e.getSource()==scorePanel.bt_gameSet) { // 게임종료 버튼 누르면 읽음
			System.out.println("게임종료 눌렀어요");
			if(isRunning == true) {
				if(JOptionPane.showConfirmDialog(this, "게임을 종료 하시겠습니까?", "게임종료", JOptionPane.YES_NO_OPTION) == 0) {
					
					isRunning = false;
				}
			}else {
				JOptionPane.showMessageDialog(this, "게임이 실행중이지 않습니다.");
			}
		}
		/* -------------------------------- 게임종료 버튼 종료 -------------------------------- */
		
		
		/* -------------------------------- 두더지 설정버튼 눌렀을 때 -------------------------------- */
		if(e.getSource()==scorePanel.bt_setLevel) {
			System.out.println("두더지 설정 눌렀어요");
			if(!isRunning) {
				
				// 두더지 마릿수 입력받기
				String getNum = JOptionPane.showInputDialog("두더지 수를 입력하세요. (1~9)");
				if(getNum!=null) {
					boolean isNum = Pattern.matches("^[1-9]*$", getNum); // 숫자 0~9까지의 정규표현식
					if(isNum) { // 숫자가 맞다면
						
						try {
							if(Integer.parseInt(getNum)<10) {
								gamePanel.tileAmount = Integer.parseInt(getNum); // 타일갯수에 받은 수 대입
							}else {
								JOptionPane.showMessageDialog(this, "1~9의 숫자만 입력하세요.");
							}
						} catch (NumberFormatException e1) {
							JOptionPane.showMessageDialog(this, "수를 입력해주세요.");
						}
						gamePanel.deleteMoles(); // 두더지수를 바꿨기 때문에 존재하는 두더지들 모두 지우고
						gamePanel.createMoles(this); // 받은 두더지수 만큼 두더지를 다시 생성 해준다
						
						// 패널 새로고침
						gamePanel.revalidate();  
						gamePanel.repaint();
					}else {
						JOptionPane.showMessageDialog(this, "1~9의 숫자만 입력하세요.");
					}
				}
				
			}else {
				JOptionPane.showMessageDialog(this, "게임이 실행중입니다.");
			}
		}
		/* -------------------------------- 두더지 설정버튼 종료 -------------------------------- */
		
		/* -------------------------------- 속도 설정버튼 눌렀을 때 -------------------------------- */
		if(e.getSource()==scorePanel.bt_setSpeed) {
			System.out.println("속도설정 눌렀니?");
			if(!isRunning) {
				
				// 속도 입력 받기
				String getSpeed = JOptionPane.showInputDialog("속도를 입력하세요. (1 = 0.3초)");
				if(getSpeed!=null) {
					boolean isNum = Pattern.matches("^[1-9]*$", getSpeed); // 숫자 0~9까지의 정규표현식
					if(isNum) { // 숫자가 맞다면
						try {
							if(Integer.parseInt(getSpeed)<10) {
								gamePanel.changeSpeed(Integer.parseInt(getSpeed));
							}else {
								JOptionPane.showMessageDialog(this, "1~9의 숫자만 입력하세요.");
							}
						} catch (NumberFormatException e1) {
							JOptionPane.showMessageDialog(this, "수를 입력해주세요.");
						}
						
					}else {
						JOptionPane.showMessageDialog(this, "1~9의 숫자만 입력하세요.");
					}
				}
				
			}else {
				JOptionPane.showMessageDialog(this, "게임이 실행중입니다.");
			}
		}
		/* -------------------------------- 속도 설정버튼 종료 -------------------------------- */
	}
	
	public void gameStart() {
		//scorePanel.bt_gameStart
		gameStartThead = new Thread(this); // 게임 시작 쓰레드
		gameStartThead.start();
	}

	@Override
	public void run() {
		int zero_sc = 0;
		int sc = 0;
		int min = 0;
		while(isRunning) {
			// 무한루프
			try {
				
				/* ---------------- 타이머 코드 시작 ----------------*/
				Thread.sleep(10);
				ticktock(zero_sc, sc, min);
				zero_sc++;
				if(zero_sc > 60) {
					zero_sc = 0;
					sc++;
				}
				if(sc > 60) {
					sc = 0;
					min++;
				}
				/* ---------------- 타이머 코드 종료 ----------------*/
				//scorePanel.repaint();
				//System.out.println("쓰레드 러닝");
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}
	
	public void ticktock(int zero_sc, int sc, int min) { // 시계 만들기
		scorePanel.time_field.setText(min+" : "+sc+" : "+zero_sc);
	}
	
	public void customCursor() { // 마우스커서도 커스텀 해보자
		Toolkit tk = Toolkit.getDefaultToolkit();
		Image cursorImage = tk.getImage("src/images/hammer.png");
		cursorImage.getScaledInstance(150, 150, Image.SCALE_SMOOTH);
		
		Point point = new Point(20, 20);
		Cursor cursor = tk.createCustomCursor(cursorImage, point, "hammer");
		this.setCursor(cursor);
	}
	
}
