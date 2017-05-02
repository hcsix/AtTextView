**前言**
--
在Android中使用TextView实现@功能主要需要用到以下两个知识点：

* 正则表达式：http://blog.csdn.net/yaerfeng/article/details/28855587
* SpannableString：http://www.jianshu.com/p/84067ad289d2


**实现思路**
--

1. 使用正则表达式找出字符串中所有 `@用户名` 部分在字符串中的起止位置;
2. 初始化一个 SpannableString ，利用第一步中获取的起止位置来设置 ClickableSpan；
3. 将 SpannableString 设置给 TextView

**具体实现**
--

一、使用正则表达式找出字符串中所有 `@用户名` 的部分

**1.1** 根据 `@用户名` 的命名匹配规则，编写正则表达式

如匹配规则为:

* 以@开头
* 用户名可为字母、下划线、中文,长度为4-30个字符
* 空格结尾

则正则表达式可定义为：

`String NAME_RULE = "@[a-zA-Z_\u4e00-\u9fa5]{4,30} ";`

PS:正则表达式必须根据需求给定的命名规则来定

**1.2** 找出字符串中所有符合正则匹配的部分，并记录其信息（开始的位置、结束的位置、文字内容）

首先我们先定义一个模型类 AtBean 来存放匹配部分的信息

**AtBean.class**



	public class AtBean {
    	//文字
    	private String name;
    	//开始的位置
    	private int startPos;
    	//结束的位置
    	private int endPos;

    	public AtBean(String name, int startPos, int endPos) {
       		this.name = name;
        	this.startPos = startPos;
        	this.endPos = endPos;
    	}

    	public String getName() {
        	return name;
    	}

    	public void setName(String name) {
        	this.name = name;
    	}

    	public int getStartPos() {
        	return startPos;
    	}

    	public void setStartPos(int startPos) {
        	this.startPos = startPos;
    	}

   		public int getEndPos() {
        	return endPos;
    	}

    	public void setEndPos(int endPos) {
        	this.endPos = endPos;
    	}

    	@Override
    	public String toString() {
        	return "name --> " + name + "  startPos --> " + startPos + "  endPos --> " + endPos;
    	}
	}


进行正则匹配，将获取到的信息保存到数组 atBeanList 中

**getAtBeanList(String str)**

    private List<AtBean> getAtBeanList(String str) {

        List<AtBean> atBeanList = new ArrayList<>();

		// 正则表达式
        String NAME_RULE = "@[a-zA-Z_\u4e00-\u9fa5]{4,30} ";

        // 编译正则表达式
        Pattern pattern = Pattern.compile(NAME_RULE);

        Matcher m = pattern.matcher(str);

        while (m.find()) {
            AtBean bean = new AtBean(m.group(), m.start(), m.end());
            atBeanList.add(bean);
            Log.i("Find AT String", bean.toString());
        }
        return atBeanList;
    }


二、设置 SpannableString 

**2.1** 新建 ClickableSpan 的子类 Clickable ，为  `@用户名` 设置样式和点击事件

**Clickable.class**

    private class Clickable extends ClickableSpan implements View.OnClickListener {
        private View.OnClickListener mListener;
        private Context context;

        private Clickable(Context context,View.OnClickListener mListener) {
            this.context = context;
            this.mListener = mListener;
        }

        //设置显示样式
        @Override
        public void updateDrawState(TextPaint ds) {
            super.updateDrawState(ds);
            ds.setColor(ContextCompat.getColor(context, R.color.colorPrimary));//设置颜色
            ds.setUnderlineText(false);//设置下划线
        }

        @Override
        public void onClick(View v) {
            mListener.onClick(v);
        }
    }


**2.2** 将 Clickable 添加到 SpannableString 中

**getClickSpannableString(String str, List<AtBean> atBeanList)**

    private SpannableString getClickSpannableString(String str, List<AtBean> atBeanList) {
        SpannableString spannableStr = new SpannableString(str);
        for (final AtBean atBean : atBeanList) {
            spannableStr.setSpan(new Clickable(MainActivity.this,new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //每个 @用户名 字符串的点击事件
                    Toast.makeText(MainActivity.this, "点击了 ————> " + atBean.getName(), Toast.LENGTH_SHORT).show();
                }
            }), atBean.getStartPos(), atBean.getEndPos(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        }
        return spannableStr;
    }

三、将 SpannableString 设置给 TextView

		List<AtBean> atBeanList = getAtBeanList(str);
        SpannableString spannableStr = getClickSpannableString(str, atBeanList);
        showTv.setText(spannableStr);
        //激活点击事件
        showTv.setMovementMethod(LinkMovementMethod.getInstance());