package whackAmole.main;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import javax.swing.JPanel;

public class GamePanel extends JPanel implements Runnable{
	// 게임 패널 클래스
	// 이 패널에서 두더지게임이 실행된다.
	ScorePanel scorePanel; // Mole이 스코어패널에 접근하기위해 생성자 매개변수로 받아올거임
	Window window;
	
	static int tileSize = 100; // 타일 사이즈
	static int tileAmount = 3; // ?x? ( 두더지 게임 타일 갯수 )
	int p_width = tileSize * tileAmount;
	int p_height = tileSize * tileAmount;
	
	List<Mole> moles = new ArrayList<Mole>();
	
	Random random = new Random(); // 랜덤값 사용을 위한 랜덤 클래스
	
	Thread moleThread; // 두더지들이 탈 쓰레드 ( 게임시작과 동시에 분주히 움직일것이다. )
	
	int upSpeed = 3; // ?x0.3초 마다 두더지가 올라옴 
 	
	public GamePanel(Window window, ScorePanel scorePanel) {
		this.scorePanel = scorePanel; // 스코어패널 받아오기
		this.window = window; // 윈도우 받아오기
		/* ------------------------ 게임패널 기본 세팅 ------------------------ */
		//this.setLayout(new GridLayout(tileAmount, tileAmount)); // 레이아웃 설정
		this.setPreferredSize(new Dimension(p_width, p_height));
		//this.setBackground(Color.BLUE);
		
		/* ------------------------ 컴포넌트 생성 ------------------------ */
		createMoles(window);
		
	}
	
	// 두더지 생성하는 메서드
	public void createMoles(Window window) {
		this.setLayout(new GridLayout(tileAmount, tileAmount)); // 두더지가 재 생성되기 전에 레이아웃을 설정해줘야 한다
		for(int i=0; i<(tileAmount*tileAmount); i++) {
			Mole mole = new Mole(i, window, scorePanel); // 두더지 생성
			//System.out.println(mole);
			moles.add(mole); // 두더지 리스트에 넣어놓기
			this.add(mole); // 두더지 패널에 조립
			//System.out.println("생성시 몰스 사이즈 : "+moles.size());
		}
	}
	
	public void deleteMoles() {
		//System.out.println("포문 시작전 사이즈 : "+moles.size());
		for(int i=0; i<moles.size(); i++) {
			this.remove(moles.get(i)); // 패널에 붙어있는 mole 버튼들 모두 제거
			//moles.remove(i);
			//System.out.println(i);
		}
		moles.clear(); // moles에 들어있는것들 모두 제거
		//System.out.println("지운 후 몰스 사이즈 : "+moles.size());
	}
	
	public void startMoleThread() {
		moleThread = new Thread(this); // 두더지 활동 시작 쓰레드
		moleThread.start();
	}
	
	public void changeSpeed(int upSpeed) {
		this.upSpeed = upSpeed;
	}

	@Override
	public void run() { // 두더지 활동 쓰레드
		
		while(Window.isRunning) {
			//System.out.println("두더지쓰레드 런");
			//System.out.println(moles.size());
			int ranNum = random.nextInt(moles.size()); // 0 ~ moles.size 까지의 랜덤값 추출
			moles.get(ranNum).isUp = true; // 두더지 빼꼼
			if(moles.get(ranNum).isUp == true) { // 두더지가 빼꼼일때
				// 두더지가 머리를 들었을때 블랙으로 변경 (들어가면 다시 초록)
				moles.get(ranNum).setBackground(Color.WHITE);
				moles.get(ranNum).setIcon(Util.getInstance().createIcon("/images/mole.png", getIconSize(), getIconSize()));
				//System.out.println("아이콘 사이즈 : "+getIconSize());
				try {
					
					moleThread.sleep(upSpeed * 300); // ?초 쉬기 ( 0.3초 의 배수 )
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
				moles.get(ranNum).isUp = false; // 두더지 들어가기
				moles.get(ranNum).setBackground(Color.WHITE); 
				moles.get(ranNum).setIcon(Util.getInstance().createIcon("/images/hole.png", getIconSize(), getIconSize())); // 두더지가 들어갔으니 아이콘 변경
			}
		}
		
	}
	
	// 윈도우에 게임패널과 두더지들을 pack() 했기때문에
	// 두더지 공간의 크기가 두더지 마릿수에따라 유동적으로 변한다.
	// 때문에 사이즈를 100으로 고정하면 두더지가 9x9마리 나올때 이미지 사이즈가 벗어나게 된다.
	// switch문으로 각 마릿수마다 사이즈를 정해주면 좋을것 같다.
	public static int getIconSize() {
		int result = 0;
		switch(tileAmount) {
			case 1 : result = 140; break;
			case 2 : result = 130; break;
			case 3 : result = 120; break;
			case 4 : result = 110; break;
			case 5 : result = 100; break;
			case 6 : result = 90; break;
			case 7 : result = 80; break;
			case 8 : result = 70; break;
			case 9 : result = 60; break;
			default : result = 100; break;
		}
		return result;
	}
	

}
