import java.awt.GridLayout;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowEvent;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;


public class Main {
	public static JButton[] menu = new JButton[11];
	public static JButton[][] grid = new JButton[11][11];
	public static int[][] board = new int[11][11];
	public static int size = 0;
	public static int sizeForWin = 0;
	public static int player = 1;
	public static ImageIcon oIcon;
	public static ImageIcon xIcon;
	public static JFrame frame;
	public static JFrame frameSel;
	
	
	
	public static void main(String[] args) {
		
		initImages();
		boardSize();
		
	}

	
	//выбор размера игры
	public static void boardSize() {
	    final JFrame frameSel = new JFrame("Выберете размер игры");
		frameSel.setSize(500, 250);
		frameSel.setVisible(true); 
		frameSel.setResizable(false);
		
		JPanel panelSel = new JPanel();
		panelSel.setSize(100, 100);
		frameSel.add(panelSel);
		panelSel.setLayout(new GridLayout(2, 4));
		
		for (int i=3;i<11;i++) {
			final int pos = i;
			menu[i] = new JButton(i + "x" + i);
		    menu[i].addActionListener(new ActionListener(){
			int id = pos;
			public void actionPerformed(ActionEvent arg0){
			size = id;
			System.out.print("size : " + size);
			if (size <= 7) sizeForWin = 3; else sizeForWin = 5;
			frameSel.dispatchEvent(new WindowEvent(frameSel, WindowEvent.WINDOW_CLOSING));
			userInterface(size);
			    }
			   });
			   panelSel.add(menu[i]);

		}
		//пастроика игровова поля
		
	}
	
	
	
	
	
	//jFrame object, s - размер поля: s на s
	public static void userInterface(int s) {
		frame = new JFrame("Player 1 (x)");
		frame.setSize(s*70, s*70);
		frame.setVisible(true); 
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setResizable(false);
		frame.setLocationRelativeTo(frameSel);
		
		
		JPanel panel = new JPanel();
		panel.setSize(70, 70);
		frame.add(panel);
		panel.setLayout(new GridLayout(s, s));
		
		for (int i = 0; i < s; i++)
			for (int j = 0; j < s;j++) {
				final int x = i, y = j;
			    grid[i][j] = new JButton();
			    grid[i][j].addActionListener(new ActionListener(){
			    int id_x = x;
			    int id_y = y;
			    public void actionPerformed(ActionEvent arg0){
			    play(id_x, id_y);
			    }
			   });
			    panel.add(grid[i][j]);
			}
							
	}
	
	
	//запись x или o
	public static void play(int x, int y) {
		System.out.print("x=" + x + ": y=" + y + "\n");
		if (board[x][y] != 0) return;
		//player 1:
		if (player == 1) {
			grid[x][y].setIcon(xIcon);
			board[x][y] = 1;
			if (searchVictory() != 0) {System.out.print("\nВыйграл игрок 1 (x)"); win(player); }
			player++;	
			frame.setTitle("Player 2 (o)");
		} else {
			grid[x][y].setIcon(oIcon);
			board[x][y] = 2;
			if (searchVictory() != 0) {System.out.print("\nВыйграл игрок 2 (o)"); win(player); }
			player--;	
			frame.setTitle("Player 1 (x)");
		}
	
	}
	

	
	// подготовка изоброжении
	public static void initImages() {
		Image imgO, imgX;
		try {
			imgO = ImageIO.read(Main.class.getResource("/o.png"));
			imgX = ImageIO.read(Main.class.getResource("/x.png"));
			oIcon = new ImageIcon(imgO.getScaledInstance(70, 70,Image.SCALE_SMOOTH));
			xIcon = new ImageIcon(imgX.getScaledInstance(70, 70,Image.SCALE_SMOOTH));
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		  
	}
	
	
	public static int searchVictory() {
		
		//поиск по горизонтали и вертикале
		int nro = 0, nrh = 0;
		for (int i = 0; i < size; i++) {
			nrh=0; nro=0;
			for (int j = 0; j < size;j++) {
				if (board[i][j] == player) {nro++; 
					if (nro == sizeForWin) return 1;}  else nro = 0;
                if (board[j][i] == player) {nrh++;  
                	if (nrh == sizeForWin) return 1;}  else nrh = 0;
			}
				}	
		
		//поиск по диагонале 1
		int nr1 = 0, nr2 = 0;
		
		for (int j = size-1; j >= 0; j--) {
			nr1=0; nr2=0; int k=j, i=0;
			while (i<size && k<size){
				 if (board[i][k] == player) {nr1++;  
	              	if (nr1 == sizeForWin) return 1;} else nr1 = 0;
				 if (board[k][i] == player) {nr2++;  
	              	if (nr2 == sizeForWin) return 1;} else nr2 = 0;
				 i++;
				 k++;
			}
		}
		
		//поиск по диагонале 2
		int nr3 = 0, nr4 = 0;
		
		for (int i = 0; i < size; i++) {
			nr3=0; nr4=0; int k=i, j=0;
			while (j<size && k<size && k>=0){
				
				 if (board[k][j] == player) {nr3++;  
	              	if (nr3 == sizeForWin) return 1;} else nr3 = 0;
				 if (board[size-k-1][size-j-1] == player) {nr4++;  
	              	if (nr4 == sizeForWin) return 1;} else nr4 = 0;
				 j++;
				 k--;
			}
		}
		draw ();
		return 0;
			}
	//показ победителя и выбор следующеего деиствия
	public static void win(int id) {
		JPanel panel = new JPanel();
		JButton butOk = new JButton("Повтор");
		JButton butExit = new JButton("Выйти");
		final JFrame frameWin = new JFrame();
		if (id == 3) frameWin.setTitle("Ничья");
		else frameWin.setTitle("Выйграл игрок " + player);
		frameWin.add(panel);
		panel.setLayout(new GridLayout(1, 2));
		panel.add(butOk);
		panel.add(butExit);
		frameWin.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frameWin.setLocationRelativeTo(frame);
		frameWin.setVisible(true);
		frameWin.setSize(300, 75);
		
		butOk.addActionListener(new ActionListener() {		 
            public void actionPerformed(ActionEvent e)
            {
            	frame.setVisible(false);
            	frameWin.setVisible(false);
            	restart();	
            }
        });      
		
		butExit.addActionListener(new ActionListener() {		 
            public void actionPerformed(ActionEvent e)
            {
            	frameWin.dispatchEvent(new WindowEvent(frameWin, WindowEvent.WINDOW_CLOSING));
            }
        });      
		
	}
	
	//обнуливание для новой игры
	public static void restart() {
		for (int i = 0; i < size; i++) 
			for (int j = 0; j < size; j++) {
				grid[i][j].setIcon(null);
				board[i][j] = 0;
			}
		player = 1;
		boardSize();
	}
	//ничья
	public static void draw () {
		int nr=0;
		for (int i = 0; i < size; i++) 
			for (int j = 0; j < size; j++) 
				if(board[i][j] == 0) nr++;
		if (nr == 0) win(3);
	}
	
	}
	
	
	
	
	
	
	


