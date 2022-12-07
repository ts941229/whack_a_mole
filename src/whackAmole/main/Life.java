package whackAmole.main;

import java.awt.Dimension;
import java.awt.Image;

import javax.swing.ImageIcon;
import javax.swing.JLabel;

public class Life extends JLabel{
	// 목숨 클래스
	
	
	public Life() {
		// 라이프 클래스 기본 셋팅
		this.setIcon(Util.getInstance().createIcon("/images/heart.png", 100, 100));
		this.setPreferredSize(new Dimension(100, 100));
		this.setHorizontalAlignment(JLabel.CENTER);
	}
	
}
