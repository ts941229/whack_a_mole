package whackAmole.main;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JOptionPane;

public class Mole extends JButton implements ActionListener{
	// 두더지 클래스
	// 두더지는 버튼으로 하면 보다 편하게 개발할 수 있을 거 같다.
	ScorePanel scorePanel; // 스코어패널에 접근하기 위해
	Window window; // 윈도우에 접근하기 위해
	
	public boolean isUp = false; // 머리가 나와있는지 안나와있는지를 불린값으로
	int moleNum = 0;
	
	
	public Mole(int moleNum, Window window, ScorePanel scorePanel) {
		this.scorePanel = scorePanel; // 스코어패널 받아오기
		this.window = window; // 윈도우 받아오기
		this.setPreferredSize(new Dimension(GamePanel.tileSize, GamePanel.tileSize));
		this.setBackground(Color.WHITE);
		this.moleNum = moleNum; // 각 두더지마다 번호를 주자
		this.addActionListener(this); // 액션리스너 조립
		
		this.setIcon(Util.getInstance().createIcon("/images/hole.png", GamePanel.getIconSize(), GamePanel.getIconSize()));
	}

	@Override
	public void actionPerformed(ActionEvent e) { // 두더지 누르면
		//System.out.println(moleNum+"번째 두더지야");
		if(Window.isRunning) { // 게임이 실행중일때만!!
			if(isUp) {
				System.out.println("잡았다!");
				
				scorePanel.score+=10;	// 스코어패널의 점수 10점씩 누적
				scorePanel.score_field.setText("점수 : "+scorePanel.score); // 스코어패널의 스코어필드 새로고침
				
				isUp = false;
				setBackground(Color.WHITE);
				this.setIcon(Util.getInstance().createIcon("/images/hole.png", GamePanel.getIconSize(), GamePanel.getIconSize()));
				//System.out.println(GamePanel.tileAmount);
			}else {
				System.out.println("나 아니라구!");
				
				scorePanel.score-=10;	// 스코어패널의 점수 10점씩 감소
				scorePanel.score_field.setText("점수 : "+scorePanel.score); // 스코어패널의 스코어필드 새로고침
				
				if(scorePanel.lifes.size() > 1) { // 만약 라이프가 있다면
					// 목숨 1개 감소
					scorePanel.deleteOneLife();
					//System.out.println("라이프 수 : "+scorePanel.lifes.size());
					// 새로고침
					scorePanel.revalidate();
					scorePanel.repaint();
				}else { // 라이프 소진시
					Window.isRunning = false; // 게임 종료
					JOptionPane.showMessageDialog(window, "게임 오버!!!");
				}
			}
		}
	}
	
}
