package com.practice;

import java.awt.Color;
import java.awt.EventQueue;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;

import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JRadioButtonMenuItem;
import javax.swing.JSeparator;
import javax.swing.JTextField;
import javax.swing.JToolBar;
import javax.swing.border.EmptyBorder;

public class GUI extends JFrame {
	private static final long serialVersionUID = -639767039479761232L;
	static final int WINDOW_WIDTH = 580; // 窗口宽度
	static final int WINDOW_HEIGHT = 330; // 窗口高度
	static final int OP_AMOUNT = 20; // 窗口内显示的算式数量
	static final int OP_COLUMN = 5; // 算式的列数
	static final int OP_WIDTH = 65; // 算式的宽度
	static final int ANSWER_WIDTH = 35; // 答案的宽度
	static final int COMPONET_HEIGHT = 25; // 算式、答案的高度

	private JPanel contentPane;
	private JTextField[] tfOp; // 显示的算式组件数组
	private JTextField[] tfAns; // 显示的答案组件数组
	private JLabel labelStatus; // 状态标签
	private JLabel labelStat; // 状态标签
	private JLabel labelCurrent; // 当前页号标签
	private JLabel labelTotal; // 总页数标签
	private JMenuItem mntmPre; // 前一页 菜单项
	private JMenuItem mntmNext; // 后一页 菜单项
	private JButton btnPre; // 前一页 工具栏按钮
	private JButton btnNext; // 后一页 工具栏按钮
	private Exercise exercise;
	private int totalPages; // 总页数
	private int currentPage; // 当前页码
	private boolean submitted;

	private final ButtonGroup buttonGroupTypes = new ButtonGroup();
	private final ButtonGroup buttonGroupAmount = new ButtonGroup();

	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					GUI frame = new GUI();
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	public GUI() {
		setResizable(false);

		setTitle("口算生成器 -Java8-刘岩");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, WINDOW_WIDTH, WINDOW_HEIGHT);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);

		JSeparator separator = new JSeparator();
		separator.setBounds(0, 234, 564, 2);
		contentPane.add(separator);

		// 以下为“第X页 共X页”标签
		JLabel label_1 = new JLabel("\u7B2C");
		label_1.setBounds(196, 201, 31, 23);
		contentPane.add(label_1);
		labelCurrent = new JLabel("1");
		labelCurrent.setBounds(218, 201, 23, 23);
		contentPane.add(labelCurrent);
		JLabel label_2 = new JLabel("\u9875");
		label_2.setBounds(240, 201, 23, 23);
		contentPane.add(label_2);
		JLabel label_3 = new JLabel("\u5171");
		label_3.setBounds(270, 201, 23, 23);
		contentPane.add(label_3);
		labelTotal = new JLabel("3");
		labelTotal.setBounds(293, 201, 23, 23);
		contentPane.add(labelTotal);
		JLabel label_4 = new JLabel("\u9875");
		label_4.setBounds(316, 201, 23, 23);
		contentPane.add(label_4);

		// 以下为 状态 和 统计信息 标签
		labelStatus = new JLabel(
				"\u72B6\u6001\uFF1A\u9898\u76EE\u7C7B\u578B-[\u4EC5\u52A0\u6CD5]  \u9898\u76EE\u6570\u91CF-[20]");
		labelStatus.setFont(new Font("宋体", Font.PLAIN, 12));
		labelStatus.setBounds(10, 243, 401, 25);
		contentPane.add(labelStatus);
		labelStat = new JLabel(
				"\u7EDF\u8BA1\uFF1A\u6B63\u786E\u9898\u6570-[-] \u9519\u8BEF\u9898\u6570-[-] \u6B63\u786E\u7387-[-] \u9519\u8BEF\u7387-[-]");
		labelStat.setFont(new Font("宋体", Font.PLAIN, 12));
		labelStat.setBounds(10, 264, 520, 25);
		contentPane.add(labelStat);

		// 以下为菜单
		JMenuBar menuBar = new JMenuBar();
		menuBar.setBounds(0, 0, 574, 25);
		contentPane.add(menuBar);
		JMenu menuFile = new JMenu("\u6587\u4EF6");
		menuBar.add(menuFile);
		JMenuItem mntnIn = new JMenuItem("\u5BFC\u5165\u9898\u76EE\u548C\u7B54\u6848");
		menuFile.add(mntnIn);
		JMenuItem mntmOut = new JMenuItem("\u5BFC\u51FA\u9898\u76EE\u548C\u7B54\u6848");
		menuFile.add(mntmOut);
		JMenu menuOption = new JMenu("\u9898\u76EE\u8BBE\u7F6E");
		menuBar.add(menuOption);
		JMenu mnTypes = new JMenu("\u6839\u636E\u7C7B\u578B\u751F\u6210");
		menuOption.add(mnTypes);
		JRadioButtonMenuItem rbtnAdd = new JRadioButtonMenuItem("\u52A0\u6CD5\u9898\u76EE");
		rbtnAdd.setSelected(true);
		buttonGroupTypes.add(rbtnAdd);
		mnTypes.add(rbtnAdd);
		JRadioButtonMenuItem rbtnSub = new JRadioButtonMenuItem("\u51CF\u6CD5\u9898\u76EE");
		rbtnSub.setSelected(true);
		buttonGroupTypes.add(rbtnSub);
		mnTypes.add(rbtnSub);
		JRadioButtonMenuItem rbtnBin = new JRadioButtonMenuItem("\u52A0\u51CF\u6DF7\u5408");
		buttonGroupTypes.add(rbtnBin);
		mnTypes.add(rbtnBin);
		JMenu mnAmount = new JMenu("\u6839\u636E\u6570\u91CF\u751F\u6210");
		menuOption.add(mnAmount);
		JRadioButtonMenuItem rbtnA20 = new JRadioButtonMenuItem("20");
		rbtnA20.setSelected(true);
		buttonGroupAmount.add(rbtnA20);
		mnAmount.add(rbtnA20);
		JRadioButtonMenuItem rbtnA40 = new JRadioButtonMenuItem("40");
		buttonGroupAmount.add(rbtnA40);
		mnAmount.add(rbtnA40);
		JRadioButtonMenuItem rbtnA60 = new JRadioButtonMenuItem("60");
		buttonGroupAmount.add(rbtnA60);
		mnAmount.add(rbtnA60);
		JRadioButtonMenuItem rbtnA80 = new JRadioButtonMenuItem("80");
		buttonGroupAmount.add(rbtnA80);
		mnAmount.add(rbtnA80);
		JRadioButtonMenuItem rbtnA100 = new JRadioButtonMenuItem("100");
		buttonGroupAmount.add(rbtnA100);
		mnAmount.add(rbtnA100);
		JMenu menuOpration = new JMenu("\u9898\u76EE\u64CD\u4F5C");
		menuBar.add(menuOpration);
		JMenuItem mntmGenerate = new JMenuItem(
				"\u91CD\u65B0\u751F\u6210\u9898\u76EE\uFF08\u7C7B\u578B\u548C\u6570\u91CF\u4E0D\u53D8\uFF09");
		menuOpration.add(mntmGenerate);

		JMenuItem mntmClear = new JMenuItem("\u6E05\u7A7A\u7B54\u6848");
		menuOpration.add(mntmClear);
		JMenuItem mntmSubmit = new JMenuItem("\u63D0\u4EA4\u7B54\u6848");
		menuOpration.add(mntmSubmit);
		JMenu menuView = new JMenu("\u67E5\u770B");
		menuBar.add(menuView);
		mntmPre = new JMenuItem("\u4E0A\u4E00\u9875");
		menuView.add(mntmPre);
		mntmNext = new JMenuItem("\u4E0B\u4E00\u9875");
		menuView.add(mntmNext);

		// 以下为菜单的动作
		mntnIn.addActionListener(new ActionListener() { // 导入题目
			public void actionPerformed(ActionEvent e) {
				impExercise();
			}
		});
		mntmOut.addActionListener(new ActionListener() { // 导出题目
			public void actionPerformed(ActionEvent e) {
				expExercise();
			}
		});
		rbtnAdd.addActionListener(new ActionListener() { // 选择仅加法
			public void actionPerformed(ActionEvent e) {
				int length = exercise.length();
				exercise.generateAdditionExercise(length);
				updateComponets();
			}
		});
		rbtnSub.addActionListener(new ActionListener() { // 选择仅减法
			public void actionPerformed(ActionEvent e) {
				int length = exercise.length();
				exercise.generateSubstractExercise(length);
				updateComponets();
			}
		});
		rbtnBin.addActionListener(new ActionListener() { // 选择加减混合
			public void actionPerformed(ActionEvent e) {
				int length = exercise.length();
				exercise.generateBinaryExercise(length);
				updateComponets();
			}
		});
		rbtnA20.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				exercise.generateWithFormerType(20);
				updateComponets();
			}
		});
		rbtnA40.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				exercise.generateWithFormerType(40);
				updateComponets();
			}
		});
		rbtnA60.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				exercise.generateWithFormerType(60);
				updateComponets();
			}
		});
		rbtnA80.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				exercise.generateWithFormerType(80);
				updateComponets();
			}
		});
		rbtnA100.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				exercise.generateWithFormerType(100);
				updateComponets();
			}
		});
		mntmGenerate.addActionListener(new ActionListener() { // 重新生成题目
			public void actionPerformed(ActionEvent e) {
				generateExercise();
			}
		});
		mntmClear.addActionListener(new ActionListener() { // 清空答案
			public void actionPerformed(ActionEvent e) {
				clearAnswers();
			}
		});
		mntmSubmit.addActionListener(new ActionListener() { // 提交答案
			public void actionPerformed(ActionEvent e) {
				judgeAnswer();
			}
		});
		mntmPre.addActionListener(new ActionListener() { // 上一页
			public void actionPerformed(ActionEvent e) {
				prePage();
			}
		});
		mntmNext.addActionListener(new ActionListener() { // 下一页
			public void actionPerformed(ActionEvent e) {
				nextPage();
			}
		});

		// 以下为工具栏
		JToolBar toolBar = new JToolBar();
		toolBar.setBounds(0, 24, 564, 25);
		toolBar.setFloatable(false);
		contentPane.add(toolBar);
		JButton btnIn = new JButton("\u5BFC\u5165");
		toolBar.add(btnIn);
		JButton btnOut = new JButton("\u5BFC\u51FA");
		toolBar.add(btnOut);
		toolBar.addSeparator();
		JButton btnGenrate = new JButton("\u91CD\u65B0\u751F\u6210");
		toolBar.add(btnGenrate);

		JButton btnClear = new JButton("\u6E05\u7A7A");
		toolBar.add(btnClear);
		JButton btnSubmit = new JButton("\u63D0\u4EA4");
		toolBar.add(btnSubmit);
		toolBar.addSeparator();
		btnPre = new JButton("\u4E0A\u4E00\u9875");
		toolBar.add(btnPre);
		btnNext = new JButton("\u4E0B\u4E00\u9875");
		toolBar.add(btnNext);

		// 以下为工具栏上的按钮动作
		btnIn.addActionListener(new ActionListener() { // 导入题目 按钮动作
			public void actionPerformed(ActionEvent arg0) {
				impExercise();
			}
		});
		btnOut.addActionListener(new ActionListener() { // 导出题目 按钮动作
			public void actionPerformed(ActionEvent e) {
				expExercise();
			}
		});
		btnGenrate.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) { // 重新生成题目 按钮动作
				generateExercise();
			}
		});
		btnClear.addActionListener(new ActionListener() { // 清空答案
			public void actionPerformed(ActionEvent e) {
				clearAnswers();
			}
		});
		btnSubmit.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) { // 提交答案 按钮动作
				judgeAnswer();
			}
		});
		btnPre.addActionListener(new ActionListener() { // 上一页 按钮动作
			public void actionPerformed(ActionEvent e) {
				prePage();
			}
		});
		btnNext.addActionListener(new ActionListener() { // 下一页 按钮动作
			public void actionPerformed(ActionEvent e) {
				nextPage();
			}
		});

		initComponets();
		updateComponets();
	}

	private void impExercise() { // 导入题目
		JFileChooser jfc = new JFileChooser();
		jfc.showOpenDialog(null);
		File file = jfc.getSelectedFile();
		try {
			exercise = Exercise.loadObject(file.getAbsolutePath());
			JOptionPane.showMessageDialog(null, "导入题目成功！", "提示", JOptionPane.INFORMATION_MESSAGE);
			this.submitted = false;
			updateComponets();
		} catch (NullPointerException npe) {

		} catch (Exception e) {
			JOptionPane.showMessageDialog(null, "导入题目失败，可能是因为选择了错误的文件", "错误", JOptionPane.ERROR_MESSAGE);
		}
	}

	private void expExercise() { // 导出题目
		JFileChooser jfc = new JFileChooser();
		jfc.showSaveDialog(null);
		File file = jfc.getSelectedFile();
		try {
			exercise.saveObject(file.getAbsolutePath());
			JOptionPane.showMessageDialog(null, "导出题目成功！", "提示", JOptionPane.INFORMATION_MESSAGE);
		} catch (NullPointerException npe) {

		} catch (Exception e) {
			JOptionPane.showMessageDialog(null, "导出题目失败，可能是因为创建文件失败", "错误", JOptionPane.ERROR_MESSAGE);
		}
	}

	private void generateExercise() { // 重新生成题目
		int length = exercise.length();
		exercise.generateWithFormerType(length);
		updateComponets();
	}

	private void judgeAnswer() { // 提交答案，判题
		this.submitted = true;
		getAnswers(this.currentPage);
		updateComponets();
	}

	private void prePage() { // 上翻一页
		getAnswers(this.currentPage);
		if (this.currentPage == this.totalPages)
			this.leaveEnd();
		if (--currentPage == 1)
			this.reachBegin();
		this.labelCurrent.setText(String.valueOf(currentPage));
		updateComponets();
	}

	private void nextPage() { // 下翻一页
		getAnswers(this.currentPage);
		if (this.currentPage == 1)
			this.leaveBegin();
		if (++currentPage == this.totalPages)
			this.reachEnd();
		this.labelCurrent.setText(String.valueOf(currentPage));
		updateComponets();

	}

	private void getAnswers(int pageIndex) {
		for (int i = 0; i < OP_AMOUNT; i++) {
			exercise.setAnswer((pageIndex - 1) * OP_AMOUNT + i, tfAns[i].getText());
		}
	}

	private void clearAnswers() {
		exercise.clearAnswers();
		this.submitted = false;
		updateComponets();
	}

	// 初始化事件
	private void initComponets() {
		this.submitted = false;
		exercise = new Exercise();
		exercise.generateAdditionExercise(OP_AMOUNT);
		this.currentPage = 1;
		this.totalPages = 1;
		this.reachBegin();
		this.reachEnd();
		// 循环创建算式和答案组建
		tfOp = new JTextField[OP_AMOUNT];
		tfAns = new JTextField[OP_AMOUNT];
		for (int i = 0; i < OP_AMOUNT; i++) {
			tfOp[i] = new JTextField();
			tfOp[i].setBounds(20 + (i % OP_COLUMN) * (OP_WIDTH + ANSWER_WIDTH + 5),
					60 + (i / OP_COLUMN) * (COMPONET_HEIGHT + 10), OP_WIDTH, COMPONET_HEIGHT);
			tfOp[i].setHorizontalAlignment(JTextField.RIGHT);
			tfOp[i].setEditable(false);
			contentPane.add(tfOp[i]);

			tfAns[i] = new JTextField();
			tfAns[i].setBounds(20 + OP_WIDTH + (i % OP_COLUMN) * (OP_WIDTH + ANSWER_WIDTH + 5),
					60 + (i / OP_COLUMN) * (COMPONET_HEIGHT + 10), ANSWER_WIDTH, COMPONET_HEIGHT);
			contentPane.add(tfAns[i]);
		}
	}

	// 设置"上一页"按钮变灰
	private void reachBegin() {
		this.btnPre.setEnabled(false);
		this.mntmPre.setEnabled(false);
	}

	// 设置"下一页"按钮变灰
	private void reachEnd() {
		this.btnNext.setEnabled(false);
		this.mntmNext.setEnabled(false);
	}

	private void leaveBegin() {
		this.btnPre.setEnabled(true);
		this.mntmPre.setEnabled(true);
	}

	private void leaveEnd() {
		this.btnNext.setEnabled(true);
		this.mntmNext.setEnabled(true);
	}

	private void updateComponets() {
		this.totalPages = exercise.length() / OP_AMOUNT;
		this.labelCurrent.setText(String.valueOf(this.currentPage));
		this.labelTotal.setText(String.valueOf(this.totalPages));
		if (this.currentPage == 1)
			this.reachBegin();
		else
			this.leaveBegin();
		if (this.currentPage == this.totalPages)
			this.reachEnd();
		else
			this.leaveEnd();

		Operation op;

		for (int i = 0; i < OP_AMOUNT; i++) {
			op = exercise.getOperation((currentPage - 1) * OP_AMOUNT + i);
			tfOp[i].setText(op.asString());
			if (!submitted) {
				tfAns[i].setBackground(Color.WHITE);
			} else {
				if (exercise.getJudgement((currentPage - 1) * OP_AMOUNT + i))
					tfAns[i].setBackground(Color.GREEN);
				else
					tfAns[i].setBackground(Color.RED);
			}
			tfAns[i].setText(exercise.getAnswer((currentPage - 1) * OP_AMOUNT + i));
			String curType = "";
			switch (exercise.getCurrentType()) {
			case BINARY:
				curType = "加减混合";
				break;
			case ADDITION:
				curType = "仅加法";
				break;
			case SUBTRACT:
				curType = "仅减法";
			}
			labelStatus.setText("状态：题目类型[" + curType + "]  题目数量[" + exercise.length() + "]");
			if (!submitted) {
				labelStat.setText("统计：点[提交答案]之后可获取");
			} else {
				int total = exercise.length();
				int correct = exercise.Correct();
				int wrong = total - correct;
				int cratio = (int) ((float) correct / total * 100);
				int wratio = 100 - cratio;
				String stat = "统计： 正确题数[" + correct + "]  错误题数[" + wrong + "]  正确率[" + cratio + "%]  错误率[" + wratio
						+ "%]";
				labelStat.setText(stat);
			}
		}
	}
}
