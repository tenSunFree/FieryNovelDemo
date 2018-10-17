package com.home.fierynoveldemo;

import android.graphics.Paint;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.TextView;

import java.util.List;


public class ItemFragment extends Fragment {

    private static final String TAG = "ItemFragment";

    private String title, content, pageNumber;
    private int number;
    private TextView titleTextView, contentTextView, pageNumberTextView;
    private List<PageEntity> pageEntityList;

    public static ItemFragment newInstance(int number) {
        ItemFragment fragment = new ItemFragment();
        Bundle bundle = new Bundle();
        bundle.putInt("number", number);
        fragment.setArguments(bundle);
        return fragment;
    }

    public ItemFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        Log.d(TAG, "onCreate");
        super.onCreate(savedInstanceState);
        pageEntityList = DataGenerator.getPageEntityList();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        Log.d(TAG, "onCreateView");
        final View view;
        Bundle bundle = getArguments();
        number = bundle.getInt("number");
        title = pageEntityList.get(number).getMainTitle();
        content = pageEntityList.get(number).getMainContent();
        pageNumber = pageEntityList.get(number).getPageNumber();
        if (title.equals("封面")) {
            view = inflater.inflate(R.layout.fragment_item_introduction, container, false);
            initializationView(view);
        } else if (!title.equals("null")) {
            view = inflater.inflate(R.layout.fragment_item_begin, container, false);
            initializationView(view);
        } else {
            view = inflater.inflate(R.layout.fragment_item_content, container, false);
            initializationView(view);
        }
        return view;
    }

    private void initializationView(View view) {
        if (title.equals("封面")) {
            contentTextView = view.findViewById(R.id.contentTextView);
            contentTextView.setText(content);
        } else if (!title.equals("null")) {
            titleTextView = view.findViewById(R.id.titleTextView);
            contentTextView = view.findViewById(R.id.contentTextView);
            pageNumberTextView = view.findViewById(R.id.pageNumberTextView);
            titleTextView.setText(title);
            contentTextView.setText(content);
            contentTextView.getViewTreeObserver().addOnGlobalLayoutListener(new OnGlobalLayoutListener());
            pageNumberTextView.setText("第" + pageNumber + "頁");
        } else {
            contentTextView = view.findViewById(R.id.contentTextView);
            pageNumberTextView = view.findViewById(R.id.pageNumberTextView);
            contentTextView.setText(content);
            contentTextView.getViewTreeObserver().addOnGlobalLayoutListener(new OnGlobalLayoutListener());
            pageNumberTextView.setText("第" + pageNumber + "頁");
        }
    }

    private class OnGlobalLayoutListener implements ViewTreeObserver.OnGlobalLayoutListener {
        @Override
        public void onGlobalLayout() {
            contentTextView.getViewTreeObserver().removeOnGlobalLayoutListener(this);
            final String newText = autoSplitText(contentTextView);
            if (!TextUtils.isEmpty(newText)) {
                contentTextView.setText(newText);
            }
        }
    }

    /**
     * 為了解決 標點符號會連同其前一個字元跳到下一行的問題, 在合適的位置將文本手動分成多行
     */
    private String autoSplitText(final TextView tv) {
        final String rawText = tv.getText().toString();  // 原始文本
        final Paint tvPaint = tv.getPaint();  // paint, 包含字体等信息
        final float tvWidth = tv.getWidth() - tv.getPaddingLeft() - tv.getPaddingRight();  // 控件可用宽度
        String[] rawTextLines = rawText.replaceAll("\r", "").split("\n");  // 将原始文本按行拆分
        StringBuilder sbNewText = new StringBuilder();
        for (String rawTextLine : rawTextLines) {
            if (tvPaint.measureText(rawTextLine) <= tvWidth) {
                sbNewText.append(rawTextLine);  // 如果整行宽度在控件可用宽度之内, 就不处理了
            } else {

                /** 如果整行宽度超过控件可用宽度, 则按字符测量, 在超过可用宽度的前一个字符处手动换行 */
                float lineWidth = 0;
                for (int cnt = 0; cnt != rawTextLine.length(); ++cnt) {
                    char ch = rawTextLine.charAt(cnt);
                    lineWidth += tvPaint.measureText(String.valueOf(ch));
                    if (lineWidth <= tvWidth) {
                        sbNewText.append(ch);
                    } else {
                        sbNewText.append("\n");
                        lineWidth = 0;
                        --cnt;
                    }
                }
            }
            sbNewText.append("\n");
        }
        if (!rawText.endsWith("\n")) {  // 把结尾多余的\n去掉
            sbNewText.deleteCharAt(sbNewText.length() - 1);
        }
        return sbNewText.toString();
    }
}
