namespace MaskTerminator
{
    partial class MainDlg
    {
        /// <summary>
        /// 必需的设计器变量。
        /// </summary>
        private System.ComponentModel.IContainer components = null;

        /// <summary>
        /// 清理所有正在使用的资源。
        /// </summary>
        /// <param name="disposing">如果应释放托管资源，为 true；否则为 false。</param>
        protected override void Dispose(bool disposing)
        {
            if (disposing && (components != null))
            {
                components.Dispose();
            }
            base.Dispose(disposing);
        }

        #region Windows 窗体设计器生成的代码

        /// <summary>
        /// 设计器支持所需的方法 - 不要
        /// 使用代码编辑器修改此方法的内容。
        /// </summary>
        private void InitializeComponent()
        {
            this.components = new System.ComponentModel.Container();
            System.ComponentModel.ComponentResourceManager resources = new System.ComponentModel.ComponentResourceManager(typeof(MainDlg));
            this.btnSend = new System.Windows.Forms.Button();
            this.btnStart = new System.Windows.Forms.Button();
            this.labelInfo = new System.Windows.Forms.Label();
            this.pbImg = new System.Windows.Forms.PictureBox();
            this.splitContainer1 = new System.Windows.Forms.SplitContainer();
            this.pic4 = new System.Windows.Forms.PictureBox();
            this.pic3 = new System.Windows.Forms.PictureBox();
            this.pic2 = new System.Windows.Forms.PictureBox();
            this.pic8 = new System.Windows.Forms.PictureBox();
            this.pic7 = new System.Windows.Forms.PictureBox();
            this.pic6 = new System.Windows.Forms.PictureBox();
            this.pic5 = new System.Windows.Forms.PictureBox();
            this.pic1 = new System.Windows.Forms.PictureBox();
            this.tbSeconds = new System.Windows.Forms.Label();
            this.tbMaskCode = new System.Windows.Forms.TextBox();
            this.lbLoginInfo = new System.Windows.Forms.Label();
            this.tbInfor = new System.Windows.Forms.TextBox();
            this.tbWaitCntr = new System.Windows.Forms.TextBox();
            this.tbInputCntr = new System.Windows.Forms.TextBox();
            this.tbErrorCntr = new System.Windows.Forms.TextBox();
            this.tbRightCntr = new System.Windows.Forms.TextBox();
            this.tbTodayCntr = new System.Windows.Forms.TextBox();
            this.lbUnInptCntr = new System.Windows.Forms.Label();
            this.lbInputorCntr = new System.Windows.Forms.Label();
            this.lbFailCntr = new System.Windows.Forms.Label();
            this.lbRightCntr = new System.Windows.Forms.Label();
            this.lbTodayTotalCntr = new System.Windows.Forms.Label();
            this.cbBeep = new System.Windows.Forms.CheckBox();
            this.cbAutoShow = new System.Windows.Forms.CheckBox();
            this.cbEscMin = new System.Windows.Forms.CheckBox();
            this.cbBlackSend = new System.Windows.Forms.CheckBox();
            this.notifyIcon1 = new System.Windows.Forms.NotifyIcon(this.components);
            this.label1 = new System.Windows.Forms.Label();
            ((System.ComponentModel.ISupportInitialize)(this.pbImg)).BeginInit();
            this.splitContainer1.Panel1.SuspendLayout();
            this.splitContainer1.Panel2.SuspendLayout();
            this.splitContainer1.SuspendLayout();
            ((System.ComponentModel.ISupportInitialize)(this.pic4)).BeginInit();
            ((System.ComponentModel.ISupportInitialize)(this.pic3)).BeginInit();
            ((System.ComponentModel.ISupportInitialize)(this.pic2)).BeginInit();
            ((System.ComponentModel.ISupportInitialize)(this.pic8)).BeginInit();
            ((System.ComponentModel.ISupportInitialize)(this.pic7)).BeginInit();
            ((System.ComponentModel.ISupportInitialize)(this.pic6)).BeginInit();
            ((System.ComponentModel.ISupportInitialize)(this.pic5)).BeginInit();
            ((System.ComponentModel.ISupportInitialize)(this.pic1)).BeginInit();
            this.SuspendLayout();
            // 
            // btnSend
            // 
            this.btnSend.BackColor = System.Drawing.Color.FromArgb(((int)(((byte)(199)))), ((int)(((byte)(237)))), ((int)(((byte)(204)))));
            this.btnSend.Font = new System.Drawing.Font("宋体", 10.5F, System.Drawing.FontStyle.Bold, System.Drawing.GraphicsUnit.Point, ((byte)(134)));
            this.btnSend.Location = new System.Drawing.Point(191, 277);
            this.btnSend.Name = "btnSend";
            this.btnSend.Size = new System.Drawing.Size(75, 30);
            this.btnSend.TabIndex = 2;
            this.btnSend.Text = "发送";
            this.btnSend.UseVisualStyleBackColor = false;
            this.btnSend.Click += new System.EventHandler(this.btnSend_Click);
            // 
            // btnStart
            // 
            this.btnStart.BackColor = System.Drawing.Color.FromArgb(((int)(((byte)(199)))), ((int)(((byte)(237)))), ((int)(((byte)(204)))));
            this.btnStart.Font = new System.Drawing.Font("宋体", 10.5F, System.Drawing.FontStyle.Bold, System.Drawing.GraphicsUnit.Point, ((byte)(134)));
            this.btnStart.Location = new System.Drawing.Point(19, 15);
            this.btnStart.Name = "btnStart";
            this.btnStart.Size = new System.Drawing.Size(75, 32);
            this.btnStart.TabIndex = 0;
            this.btnStart.Text = "开始";
            this.btnStart.UseVisualStyleBackColor = false;
            this.btnStart.Click += new System.EventHandler(this.btnStart_Click);
            // 
            // labelInfo
            // 
            this.labelInfo.AutoSize = true;
            this.labelInfo.BackColor = System.Drawing.Color.FromArgb(((int)(((byte)(199)))), ((int)(((byte)(237)))), ((int)(((byte)(204)))));
            this.labelInfo.Font = new System.Drawing.Font("宋体", 10.5F, System.Drawing.FontStyle.Bold, System.Drawing.GraphicsUnit.Point, ((byte)(134)));
            this.labelInfo.Location = new System.Drawing.Point(109, 24);
            this.labelInfo.Name = "labelInfo";
            this.labelInfo.Size = new System.Drawing.Size(136, 14);
            this.labelInfo.TabIndex = 4;
            this.labelInfo.Text = "欢迎使用19e打码器";
            // 
            // pbImg
            // 
            this.pbImg.Location = new System.Drawing.Point(19, 58);
            this.pbImg.Name = "pbImg";
            this.pbImg.Size = new System.Drawing.Size(293, 190);
            this.pbImg.TabIndex = 2;
            this.pbImg.TabStop = false;
            this.pbImg.MouseDown += new System.Windows.Forms.MouseEventHandler(this.pbImg_MouseDown);
            // 
            // splitContainer1
            // 
            this.splitContainer1.BorderStyle = System.Windows.Forms.BorderStyle.FixedSingle;
            this.splitContainer1.Location = new System.Drawing.Point(12, 12);
            this.splitContainer1.Name = "splitContainer1";
            // 
            // splitContainer1.Panel1
            // 
            this.splitContainer1.Panel1.Controls.Add(this.pic4);
            this.splitContainer1.Panel1.Controls.Add(this.pic3);
            this.splitContainer1.Panel1.Controls.Add(this.pic2);
            this.splitContainer1.Panel1.Controls.Add(this.pic8);
            this.splitContainer1.Panel1.Controls.Add(this.pic7);
            this.splitContainer1.Panel1.Controls.Add(this.pic6);
            this.splitContainer1.Panel1.Controls.Add(this.pic5);
            this.splitContainer1.Panel1.Controls.Add(this.pic1);
            this.splitContainer1.Panel1.Controls.Add(this.tbSeconds);
            this.splitContainer1.Panel1.Controls.Add(this.tbMaskCode);
            this.splitContainer1.Panel1.Controls.Add(this.labelInfo);
            this.splitContainer1.Panel1.Controls.Add(this.btnSend);
            this.splitContainer1.Panel1.Controls.Add(this.btnStart);
            this.splitContainer1.Panel1.Controls.Add(this.pbImg);
            // 
            // splitContainer1.Panel2
            // 
            this.splitContainer1.Panel2.BackColor = System.Drawing.Color.FromArgb(((int)(((byte)(199)))), ((int)(((byte)(237)))), ((int)(((byte)(204)))));
            this.splitContainer1.Panel2.Controls.Add(this.lbLoginInfo);
            this.splitContainer1.Panel2.Controls.Add(this.tbInfor);
            this.splitContainer1.Panel2.Controls.Add(this.tbWaitCntr);
            this.splitContainer1.Panel2.Controls.Add(this.tbInputCntr);
            this.splitContainer1.Panel2.Controls.Add(this.tbErrorCntr);
            this.splitContainer1.Panel2.Controls.Add(this.tbRightCntr);
            this.splitContainer1.Panel2.Controls.Add(this.tbTodayCntr);
            this.splitContainer1.Panel2.Controls.Add(this.lbUnInptCntr);
            this.splitContainer1.Panel2.Controls.Add(this.lbInputorCntr);
            this.splitContainer1.Panel2.Controls.Add(this.lbFailCntr);
            this.splitContainer1.Panel2.Controls.Add(this.lbRightCntr);
            this.splitContainer1.Panel2.Controls.Add(this.lbTodayTotalCntr);
            this.splitContainer1.Panel2.Controls.Add(this.cbBeep);
            this.splitContainer1.Panel2.Controls.Add(this.cbAutoShow);
            this.splitContainer1.Panel2.Controls.Add(this.cbEscMin);
            this.splitContainer1.Panel2.Controls.Add(this.cbBlackSend);
            this.splitContainer1.Panel2.Font = new System.Drawing.Font("宋体", 15F, System.Drawing.FontStyle.Bold, System.Drawing.GraphicsUnit.Point, ((byte)(134)));
            this.splitContainer1.Size = new System.Drawing.Size(520, 326);
            this.splitContainer1.SplitterDistance = 321;
            this.splitContainer1.TabIndex = 5;
            // 
            // pic4
            // 
            this.pic4.BackColor = System.Drawing.Color.Transparent;
            this.pic4.BackgroundImage = ((System.Drawing.Image)(resources.GetObject("pic4.BackgroundImage")));
            this.pic4.Enabled = false;
            this.pic4.Image = ((System.Drawing.Image)(resources.GetObject("pic4.Image")));
            this.pic4.InitialImage = ((System.Drawing.Image)(resources.GetObject("pic4.InitialImage")));
            this.pic4.Location = new System.Drawing.Point(242, 60);
            this.pic4.Name = "pic4";
            this.pic4.Size = new System.Drawing.Size(41, 31);
            this.pic4.SizeMode = System.Windows.Forms.PictureBoxSizeMode.AutoSize;
            this.pic4.TabIndex = 6;
            this.pic4.TabStop = false;
            this.pic4.Visible = false;
            // 
            // pic3
            // 
            this.pic3.BackColor = System.Drawing.Color.Transparent;
            this.pic3.BackgroundImage = ((System.Drawing.Image)(resources.GetObject("pic3.BackgroundImage")));
            this.pic3.Enabled = false;
            this.pic3.Image = ((System.Drawing.Image)(resources.GetObject("pic3.Image")));
            this.pic3.InitialImage = ((System.Drawing.Image)(resources.GetObject("pic3.InitialImage")));
            this.pic3.Location = new System.Drawing.Point(168, 60);
            this.pic3.Name = "pic3";
            this.pic3.Size = new System.Drawing.Size(41, 31);
            this.pic3.SizeMode = System.Windows.Forms.PictureBoxSizeMode.AutoSize;
            this.pic3.TabIndex = 6;
            this.pic3.TabStop = false;
            this.pic3.Visible = false;
            // 
            // pic2
            // 
            this.pic2.BackColor = System.Drawing.Color.Transparent;
            this.pic2.BackgroundImage = ((System.Drawing.Image)(resources.GetObject("pic2.BackgroundImage")));
            this.pic2.Enabled = false;
            this.pic2.Image = ((System.Drawing.Image)(resources.GetObject("pic2.Image")));
            this.pic2.InitialImage = ((System.Drawing.Image)(resources.GetObject("pic2.InitialImage")));
            this.pic2.Location = new System.Drawing.Point(94, 60);
            this.pic2.Name = "pic2";
            this.pic2.Size = new System.Drawing.Size(41, 31);
            this.pic2.SizeMode = System.Windows.Forms.PictureBoxSizeMode.AutoSize;
            this.pic2.TabIndex = 6;
            this.pic2.TabStop = false;
            this.pic2.Visible = false;
            // 
            // pic8
            // 
            this.pic8.BackColor = System.Drawing.Color.Transparent;
            this.pic8.BackgroundImage = ((System.Drawing.Image)(resources.GetObject("pic8.BackgroundImage")));
            this.pic8.Enabled = false;
            this.pic8.Image = ((System.Drawing.Image)(resources.GetObject("pic8.Image")));
            this.pic8.InitialImage = ((System.Drawing.Image)(resources.GetObject("pic8.InitialImage")));
            this.pic8.Location = new System.Drawing.Point(242, 133);
            this.pic8.Name = "pic8";
            this.pic8.Size = new System.Drawing.Size(41, 31);
            this.pic8.SizeMode = System.Windows.Forms.PictureBoxSizeMode.AutoSize;
            this.pic8.TabIndex = 6;
            this.pic8.TabStop = false;
            this.pic8.Visible = false;
            // 
            // pic7
            // 
            this.pic7.BackColor = System.Drawing.Color.Transparent;
            this.pic7.BackgroundImage = ((System.Drawing.Image)(resources.GetObject("pic7.BackgroundImage")));
            this.pic7.Enabled = false;
            this.pic7.Image = ((System.Drawing.Image)(resources.GetObject("pic7.Image")));
            this.pic7.InitialImage = ((System.Drawing.Image)(resources.GetObject("pic7.InitialImage")));
            this.pic7.Location = new System.Drawing.Point(168, 133);
            this.pic7.Name = "pic7";
            this.pic7.Size = new System.Drawing.Size(41, 31);
            this.pic7.SizeMode = System.Windows.Forms.PictureBoxSizeMode.AutoSize;
            this.pic7.TabIndex = 6;
            this.pic7.TabStop = false;
            this.pic7.Visible = false;
            // 
            // pic6
            // 
            this.pic6.BackColor = System.Drawing.Color.Transparent;
            this.pic6.BackgroundImage = ((System.Drawing.Image)(resources.GetObject("pic6.BackgroundImage")));
            this.pic6.Enabled = false;
            this.pic6.Image = ((System.Drawing.Image)(resources.GetObject("pic6.Image")));
            this.pic6.InitialImage = ((System.Drawing.Image)(resources.GetObject("pic6.InitialImage")));
            this.pic6.Location = new System.Drawing.Point(94, 133);
            this.pic6.Name = "pic6";
            this.pic6.Size = new System.Drawing.Size(41, 31);
            this.pic6.SizeMode = System.Windows.Forms.PictureBoxSizeMode.AutoSize;
            this.pic6.TabIndex = 6;
            this.pic6.TabStop = false;
            this.pic6.Visible = false;
            // 
            // pic5
            // 
            this.pic5.BackColor = System.Drawing.Color.Transparent;
            this.pic5.BackgroundImage = ((System.Drawing.Image)(resources.GetObject("pic5.BackgroundImage")));
            this.pic5.Enabled = false;
            this.pic5.Image = ((System.Drawing.Image)(resources.GetObject("pic5.Image")));
            this.pic5.InitialImage = ((System.Drawing.Image)(resources.GetObject("pic5.InitialImage")));
            this.pic5.Location = new System.Drawing.Point(20, 133);
            this.pic5.Name = "pic5";
            this.pic5.Size = new System.Drawing.Size(41, 31);
            this.pic5.SizeMode = System.Windows.Forms.PictureBoxSizeMode.AutoSize;
            this.pic5.TabIndex = 6;
            this.pic5.TabStop = false;
            this.pic5.Visible = false;
            // 
            // pic1
            // 
            this.pic1.BackColor = System.Drawing.Color.Transparent;
            this.pic1.BackgroundImage = ((System.Drawing.Image)(resources.GetObject("pic1.BackgroundImage")));
            this.pic1.Enabled = false;
            this.pic1.Image = ((System.Drawing.Image)(resources.GetObject("pic1.Image")));
            this.pic1.InitialImage = ((System.Drawing.Image)(resources.GetObject("pic1.InitialImage")));
            this.pic1.Location = new System.Drawing.Point(20, 60);
            this.pic1.Name = "pic1";
            this.pic1.Size = new System.Drawing.Size(41, 31);
            this.pic1.SizeMode = System.Windows.Forms.PictureBoxSizeMode.AutoSize;
            this.pic1.TabIndex = 6;
            this.pic1.TabStop = false;
            this.pic1.Visible = false;
            // 
            // tbSeconds
            // 
            this.tbSeconds.AutoSize = true;
            this.tbSeconds.Font = new System.Drawing.Font("黑体", 21.75F, System.Drawing.FontStyle.Regular, System.Drawing.GraphicsUnit.Point, ((byte)(134)));
            this.tbSeconds.ForeColor = System.Drawing.Color.Red;
            this.tbSeconds.Location = new System.Drawing.Point(273, 277);
            this.tbSeconds.Name = "tbSeconds";
            this.tbSeconds.Size = new System.Drawing.Size(0, 29);
            this.tbSeconds.TabIndex = 5;
            // 
            // tbMaskCode
            // 
            this.tbMaskCode.BackColor = System.Drawing.Color.FromArgb(((int)(((byte)(199)))), ((int)(((byte)(237)))), ((int)(((byte)(204)))));
            this.tbMaskCode.Font = new System.Drawing.Font("宋体", 10.5F, System.Drawing.FontStyle.Bold, System.Drawing.GraphicsUnit.Point, ((byte)(134)));
            this.tbMaskCode.Location = new System.Drawing.Point(19, 281);
            this.tbMaskCode.Name = "tbMaskCode";
            this.tbMaskCode.Size = new System.Drawing.Size(166, 23);
            this.tbMaskCode.TabIndex = 1;
            // 
            // lbLoginInfo
            // 
            this.lbLoginInfo.AutoSize = true;
            this.lbLoginInfo.Font = new System.Drawing.Font("宋体", 10F, System.Drawing.FontStyle.Regular, System.Drawing.GraphicsUnit.Point, ((byte)(134)));
            this.lbLoginInfo.Location = new System.Drawing.Point(11, 10);
            this.lbLoginInfo.Name = "lbLoginInfo";
            this.lbLoginInfo.Size = new System.Drawing.Size(0, 14);
            this.lbLoginInfo.TabIndex = 10;
            // 
            // tbInfor
            // 
            this.tbInfor.BackColor = System.Drawing.Color.FromArgb(((int)(((byte)(199)))), ((int)(((byte)(237)))), ((int)(((byte)(204)))));
            this.tbInfor.BorderStyle = System.Windows.Forms.BorderStyle.None;
            this.tbInfor.Font = new System.Drawing.Font("宋体", 15F, System.Drawing.FontStyle.Bold, System.Drawing.GraphicsUnit.Point, ((byte)(134)));
            this.tbInfor.ForeColor = System.Drawing.Color.Red;
            this.tbInfor.Location = new System.Drawing.Point(14, 279);
            this.tbInfor.Name = "tbInfor";
            this.tbInfor.ReadOnly = true;
            this.tbInfor.Size = new System.Drawing.Size(143, 23);
            this.tbInfor.TabIndex = 9;
            // 
            // tbWaitCntr
            // 
            this.tbWaitCntr.BackColor = System.Drawing.Color.FromArgb(((int)(((byte)(199)))), ((int)(((byte)(237)))), ((int)(((byte)(204)))));
            this.tbWaitCntr.BorderStyle = System.Windows.Forms.BorderStyle.None;
            this.tbWaitCntr.Font = new System.Drawing.Font("宋体", 10.5F, System.Drawing.FontStyle.Bold, System.Drawing.GraphicsUnit.Point, ((byte)(134)));
            this.tbWaitCntr.Location = new System.Drawing.Point(105, 250);
            this.tbWaitCntr.Name = "tbWaitCntr";
            this.tbWaitCntr.ReadOnly = true;
            this.tbWaitCntr.Size = new System.Drawing.Size(49, 16);
            this.tbWaitCntr.TabIndex = 9;
            // 
            // tbInputCntr
            // 
            this.tbInputCntr.BackColor = System.Drawing.Color.FromArgb(((int)(((byte)(199)))), ((int)(((byte)(237)))), ((int)(((byte)(204)))));
            this.tbInputCntr.BorderStyle = System.Windows.Forms.BorderStyle.None;
            this.tbInputCntr.Font = new System.Drawing.Font("宋体", 10.5F, System.Drawing.FontStyle.Bold, System.Drawing.GraphicsUnit.Point, ((byte)(134)));
            this.tbInputCntr.Location = new System.Drawing.Point(105, 222);
            this.tbInputCntr.Name = "tbInputCntr";
            this.tbInputCntr.ReadOnly = true;
            this.tbInputCntr.Size = new System.Drawing.Size(49, 16);
            this.tbInputCntr.TabIndex = 9;
            // 
            // tbErrorCntr
            // 
            this.tbErrorCntr.BackColor = System.Drawing.Color.FromArgb(((int)(((byte)(199)))), ((int)(((byte)(237)))), ((int)(((byte)(204)))));
            this.tbErrorCntr.BorderStyle = System.Windows.Forms.BorderStyle.None;
            this.tbErrorCntr.Font = new System.Drawing.Font("宋体", 10.5F, System.Drawing.FontStyle.Bold, System.Drawing.GraphicsUnit.Point, ((byte)(134)));
            this.tbErrorCntr.ForeColor = System.Drawing.Color.Red;
            this.tbErrorCntr.Location = new System.Drawing.Point(105, 191);
            this.tbErrorCntr.Name = "tbErrorCntr";
            this.tbErrorCntr.ReadOnly = true;
            this.tbErrorCntr.Size = new System.Drawing.Size(49, 16);
            this.tbErrorCntr.TabIndex = 9;
            // 
            // tbRightCntr
            // 
            this.tbRightCntr.BackColor = System.Drawing.Color.FromArgb(((int)(((byte)(199)))), ((int)(((byte)(237)))), ((int)(((byte)(204)))));
            this.tbRightCntr.BorderStyle = System.Windows.Forms.BorderStyle.None;
            this.tbRightCntr.Font = new System.Drawing.Font("宋体", 10.5F, System.Drawing.FontStyle.Bold, System.Drawing.GraphicsUnit.Point, ((byte)(134)));
            this.tbRightCntr.Location = new System.Drawing.Point(105, 160);
            this.tbRightCntr.Name = "tbRightCntr";
            this.tbRightCntr.ReadOnly = true;
            this.tbRightCntr.Size = new System.Drawing.Size(49, 16);
            this.tbRightCntr.TabIndex = 9;
            // 
            // tbTodayCntr
            // 
            this.tbTodayCntr.BackColor = System.Drawing.Color.FromArgb(((int)(((byte)(199)))), ((int)(((byte)(237)))), ((int)(((byte)(204)))));
            this.tbTodayCntr.BorderStyle = System.Windows.Forms.BorderStyle.None;
            this.tbTodayCntr.Font = new System.Drawing.Font("宋体", 10.5F, System.Drawing.FontStyle.Bold, System.Drawing.GraphicsUnit.Point, ((byte)(134)));
            this.tbTodayCntr.ForeColor = System.Drawing.Color.Red;
            this.tbTodayCntr.Location = new System.Drawing.Point(106, 132);
            this.tbTodayCntr.Name = "tbTodayCntr";
            this.tbTodayCntr.ReadOnly = true;
            this.tbTodayCntr.Size = new System.Drawing.Size(49, 16);
            this.tbTodayCntr.TabIndex = 9;
            // 
            // lbUnInptCntr
            // 
            this.lbUnInptCntr.AutoSize = true;
            this.lbUnInptCntr.BackColor = System.Drawing.Color.FromArgb(((int)(((byte)(199)))), ((int)(((byte)(237)))), ((int)(((byte)(204)))));
            this.lbUnInptCntr.Font = new System.Drawing.Font("宋体", 10.5F, System.Drawing.FontStyle.Bold, System.Drawing.GraphicsUnit.Point, ((byte)(134)));
            this.lbUnInptCntr.Location = new System.Drawing.Point(14, 251);
            this.lbUnInptCntr.Name = "lbUnInptCntr";
            this.lbUnInptCntr.Size = new System.Drawing.Size(67, 14);
            this.lbUnInptCntr.TabIndex = 7;
            this.lbUnInptCntr.Text = "待打图片";
            // 
            // lbInputorCntr
            // 
            this.lbInputorCntr.AutoSize = true;
            this.lbInputorCntr.BackColor = System.Drawing.Color.FromArgb(((int)(((byte)(199)))), ((int)(((byte)(237)))), ((int)(((byte)(204)))));
            this.lbInputorCntr.Font = new System.Drawing.Font("宋体", 10.5F, System.Drawing.FontStyle.Bold, System.Drawing.GraphicsUnit.Point, ((byte)(134)));
            this.lbInputorCntr.Location = new System.Drawing.Point(16, 223);
            this.lbInputorCntr.Name = "lbInputorCntr";
            this.lbInputorCntr.Size = new System.Drawing.Size(67, 14);
            this.lbInputorCntr.TabIndex = 6;
            this.lbInputorCntr.Text = "打码人员";
            // 
            // lbFailCntr
            // 
            this.lbFailCntr.AutoSize = true;
            this.lbFailCntr.BackColor = System.Drawing.Color.FromArgb(((int)(((byte)(199)))), ((int)(((byte)(237)))), ((int)(((byte)(204)))));
            this.lbFailCntr.Font = new System.Drawing.Font("宋体", 10.5F, System.Drawing.FontStyle.Bold, System.Drawing.GraphicsUnit.Point, ((byte)(134)));
            this.lbFailCntr.ForeColor = System.Drawing.Color.Red;
            this.lbFailCntr.Location = new System.Drawing.Point(14, 192);
            this.lbFailCntr.Name = "lbFailCntr";
            this.lbFailCntr.Size = new System.Drawing.Size(67, 14);
            this.lbFailCntr.TabIndex = 5;
            this.lbFailCntr.Text = "错误个数";
            // 
            // lbRightCntr
            // 
            this.lbRightCntr.AutoSize = true;
            this.lbRightCntr.BackColor = System.Drawing.Color.FromArgb(((int)(((byte)(199)))), ((int)(((byte)(237)))), ((int)(((byte)(204)))));
            this.lbRightCntr.Font = new System.Drawing.Font("宋体", 10.5F, System.Drawing.FontStyle.Bold, System.Drawing.GraphicsUnit.Point, ((byte)(134)));
            this.lbRightCntr.Location = new System.Drawing.Point(14, 161);
            this.lbRightCntr.Name = "lbRightCntr";
            this.lbRightCntr.Size = new System.Drawing.Size(67, 14);
            this.lbRightCntr.TabIndex = 4;
            this.lbRightCntr.Text = "正确个数";
            // 
            // lbTodayTotalCntr
            // 
            this.lbTodayTotalCntr.AutoSize = true;
            this.lbTodayTotalCntr.BackColor = System.Drawing.Color.FromArgb(((int)(((byte)(199)))), ((int)(((byte)(237)))), ((int)(((byte)(204)))));
            this.lbTodayTotalCntr.Font = new System.Drawing.Font("宋体", 10.5F, System.Drawing.FontStyle.Bold, System.Drawing.GraphicsUnit.Point, ((byte)(134)));
            this.lbTodayTotalCntr.ForeColor = System.Drawing.Color.Red;
            this.lbTodayTotalCntr.Location = new System.Drawing.Point(14, 133);
            this.lbTodayTotalCntr.Name = "lbTodayTotalCntr";
            this.lbTodayTotalCntr.Size = new System.Drawing.Size(67, 14);
            this.lbTodayTotalCntr.TabIndex = 3;
            this.lbTodayTotalCntr.Text = "今日打码";
            // 
            // cbBeep
            // 
            this.cbBeep.AutoSize = true;
            this.cbBeep.BackColor = System.Drawing.Color.FromArgb(((int)(((byte)(199)))), ((int)(((byte)(237)))), ((int)(((byte)(204)))));
            this.cbBeep.Checked = true;
            this.cbBeep.CheckState = System.Windows.Forms.CheckState.Checked;
            this.cbBeep.Font = new System.Drawing.Font("宋体", 10.5F, System.Drawing.FontStyle.Bold, System.Drawing.GraphicsUnit.Point, ((byte)(134)));
            this.cbBeep.Location = new System.Drawing.Point(14, 102);
            this.cbBeep.Name = "cbBeep";
            this.cbBeep.Size = new System.Drawing.Size(88, 18);
            this.cbBeep.TabIndex = 2;
            this.cbBeep.Text = "beep声音";
            this.cbBeep.UseVisualStyleBackColor = false;
            // 
            // cbAutoShow
            // 
            this.cbAutoShow.AutoSize = true;
            this.cbAutoShow.BackColor = System.Drawing.Color.FromArgb(((int)(((byte)(199)))), ((int)(((byte)(237)))), ((int)(((byte)(204)))));
            this.cbAutoShow.Checked = true;
            this.cbAutoShow.CheckState = System.Windows.Forms.CheckState.Checked;
            this.cbAutoShow.Font = new System.Drawing.Font("宋体", 10.5F, System.Drawing.FontStyle.Bold, System.Drawing.GraphicsUnit.Point, ((byte)(134)));
            this.cbAutoShow.Location = new System.Drawing.Point(14, 78);
            this.cbAutoShow.Name = "cbAutoShow";
            this.cbAutoShow.Size = new System.Drawing.Size(116, 18);
            this.cbAutoShow.TabIndex = 2;
            this.cbAutoShow.Text = "有码自动弹出";
            this.cbAutoShow.UseVisualStyleBackColor = false;
            // 
            // cbEscMin
            // 
            this.cbEscMin.AutoSize = true;
            this.cbEscMin.BackColor = System.Drawing.Color.FromArgb(((int)(((byte)(199)))), ((int)(((byte)(237)))), ((int)(((byte)(204)))));
            this.cbEscMin.Checked = true;
            this.cbEscMin.CheckState = System.Windows.Forms.CheckState.Checked;
            this.cbEscMin.Font = new System.Drawing.Font("宋体", 10.5F, System.Drawing.FontStyle.Bold, System.Drawing.GraphicsUnit.Point, ((byte)(134)));
            this.cbEscMin.Location = new System.Drawing.Point(14, 54);
            this.cbEscMin.Name = "cbEscMin";
            this.cbEscMin.Size = new System.Drawing.Size(95, 18);
            this.cbEscMin.TabIndex = 1;
            this.cbEscMin.Text = "ESC最小化";
            this.cbEscMin.UseVisualStyleBackColor = false;
            // 
            // cbBlackSend
            // 
            this.cbBlackSend.AutoSize = true;
            this.cbBlackSend.BackColor = System.Drawing.Color.FromArgb(((int)(((byte)(199)))), ((int)(((byte)(237)))), ((int)(((byte)(204)))));
            this.cbBlackSend.Checked = true;
            this.cbBlackSend.CheckState = System.Windows.Forms.CheckState.Checked;
            this.cbBlackSend.Font = new System.Drawing.Font("宋体", 10.5F, System.Drawing.FontStyle.Bold, System.Drawing.GraphicsUnit.Point, ((byte)(134)));
            this.cbBlackSend.Location = new System.Drawing.Point(14, 32);
            this.cbBlackSend.Name = "cbBlackSend";
            this.cbBlackSend.Size = new System.Drawing.Size(86, 18);
            this.cbBlackSend.TabIndex = 0;
            this.cbBlackSend.Text = "空格发送";
            this.cbBlackSend.UseVisualStyleBackColor = false;
            // 
            // notifyIcon1
            // 
            this.notifyIcon1.Icon = ((System.Drawing.Icon)(resources.GetObject("notifyIcon1.Icon")));
            this.notifyIcon1.Text = "19e打码器";
            this.notifyIcon1.Visible = true;
            this.notifyIcon1.MouseDoubleClick += new System.Windows.Forms.MouseEventHandler(this.notifyIcon1_MouseDoubleClick);
            // 
            // label1
            // 
            this.label1.AutoSize = true;
            this.label1.Location = new System.Drawing.Point(507, 0);
            this.label1.Name = "label1";
            this.label1.Size = new System.Drawing.Size(29, 12);
            this.label1.TabIndex = 6;
            this.label1.Text = "v2.2";
            // 
            // MainDlg
            // 
            this.AutoScaleDimensions = new System.Drawing.SizeF(6F, 12F);
            this.AutoScaleMode = System.Windows.Forms.AutoScaleMode.Font;
            this.BackColor = System.Drawing.Color.FromArgb(((int)(((byte)(199)))), ((int)(((byte)(237)))), ((int)(((byte)(204)))));
            this.ClientSize = new System.Drawing.Size(544, 349);
            this.Controls.Add(this.label1);
            this.Controls.Add(this.splitContainer1);
            this.FormBorderStyle = System.Windows.Forms.FormBorderStyle.FixedDialog;
            this.Icon = ((System.Drawing.Icon)(resources.GetObject("$this.Icon")));
            this.MaximizeBox = false;
            this.Name = "MainDlg";
            this.StartPosition = System.Windows.Forms.FormStartPosition.CenterScreen;
            this.Text = "打码器";
            this.Load += new System.EventHandler(this.MainDlg_Load);
            this.SizeChanged += new System.EventHandler(this.MainDlg_SizeChanged);
            this.FormClosing += new System.Windows.Forms.FormClosingEventHandler(this.MainDlg_FormClosing);
            ((System.ComponentModel.ISupportInitialize)(this.pbImg)).EndInit();
            this.splitContainer1.Panel1.ResumeLayout(false);
            this.splitContainer1.Panel1.PerformLayout();
            this.splitContainer1.Panel2.ResumeLayout(false);
            this.splitContainer1.Panel2.PerformLayout();
            this.splitContainer1.ResumeLayout(false);
            ((System.ComponentModel.ISupportInitialize)(this.pic4)).EndInit();
            ((System.ComponentModel.ISupportInitialize)(this.pic3)).EndInit();
            ((System.ComponentModel.ISupportInitialize)(this.pic2)).EndInit();
            ((System.ComponentModel.ISupportInitialize)(this.pic8)).EndInit();
            ((System.ComponentModel.ISupportInitialize)(this.pic7)).EndInit();
            ((System.ComponentModel.ISupportInitialize)(this.pic6)).EndInit();
            ((System.ComponentModel.ISupportInitialize)(this.pic5)).EndInit();
            ((System.ComponentModel.ISupportInitialize)(this.pic1)).EndInit();
            this.ResumeLayout(false);
            this.PerformLayout();

        }

        #endregion

        private System.Windows.Forms.Button btnSend;
        private System.Windows.Forms.Button btnStart;
        private System.Windows.Forms.Label labelInfo;
        private System.Windows.Forms.PictureBox pbImg;
        private System.Windows.Forms.SplitContainer splitContainer1;
        private System.Windows.Forms.TextBox tbMaskCode;
        private System.Windows.Forms.Label lbUnInptCntr;
        private System.Windows.Forms.Label lbInputorCntr;
        private System.Windows.Forms.Label lbFailCntr;
        private System.Windows.Forms.Label lbRightCntr;
        private System.Windows.Forms.Label lbTodayTotalCntr;
        private System.Windows.Forms.CheckBox cbAutoShow;
        private System.Windows.Forms.CheckBox cbEscMin;
        private System.Windows.Forms.CheckBox cbBlackSend;
        private System.Windows.Forms.TextBox tbInfor;
        private System.Windows.Forms.TextBox tbWaitCntr;
        private System.Windows.Forms.TextBox tbInputCntr;
        private System.Windows.Forms.TextBox tbErrorCntr;
        private System.Windows.Forms.TextBox tbRightCntr;
        private System.Windows.Forms.TextBox tbTodayCntr;
        private System.Windows.Forms.NotifyIcon notifyIcon1;
        private System.Windows.Forms.Label tbSeconds;
        private System.Windows.Forms.CheckBox cbBeep;
        private System.Windows.Forms.PictureBox pic4;
        private System.Windows.Forms.PictureBox pic3;
        private System.Windows.Forms.PictureBox pic2;
        private System.Windows.Forms.PictureBox pic8;
        private System.Windows.Forms.PictureBox pic7;
        private System.Windows.Forms.PictureBox pic6;
        private System.Windows.Forms.PictureBox pic5;
        private System.Windows.Forms.PictureBox pic1;
        private System.Windows.Forms.Label label1;
        private System.Windows.Forms.Label lbLoginInfo;
    }
}

