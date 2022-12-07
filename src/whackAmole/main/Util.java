package whackAmole.main;

import java.awt.Image;

import javax.swing.ImageIcon;

public class Util {
	// 싱글톤 패턴
	private static Util util;
	private Util() {}
	public static Util getInstance() {
		if(util == null) {
			return util = new Util(); 
		}
		return util;
	}
	
	// 아이콘 만드는 메서드
	public ImageIcon createIcon(String path, int width, int height) {
		// 아이콘 생성
		ImageIcon icon = new ImageIcon(Life.class.getResource(path));
		// ImageIcon은 객체의 크기를 직접 조절할 수 없기 때문에 
		// image로 변환후 메서드를 사용해 이미지 크기를 조절하자!
		Image image = icon.getImage().getScaledInstance(width, height, Image.SCALE_SMOOTH);
		ImageIcon result = new ImageIcon(image); // 조절된 이미지로 다시 이미지아이콘 생성
		return result;
	}
	
}
