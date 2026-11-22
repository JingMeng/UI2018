package com.zinc.code_x2_recycleview.recycler;

import android.os.Bundle;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.util.Log;
import android.util.SparseArray;
import android.view.View;
import android.widget.ScrollView;
import android.widget.TextView;

import com.zinc.code_x2_recycleview.R;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * @author : Jiang zinc
 * @version :
 * @email : 56002982@qq.com
 * @time : 2019/2/28 涓嬪崍4:48
 * @desc :
 */
public class RecyclerActivity extends AppCompatActivity implements RecyclerAdapter.BindListener {

    private final static String TAG = RecyclerActivity.class.getSimpleName();

    private RecycleViewWrapper mRvWrapper;
    private TextView mTvCacheLog;
    private TextView mTvBindLog;
    private ScrollView mSvBindLog;

    private StringBuilder logString;

    private RecyclerAdapter mAdapter;
    private final List<RecyclerBean> mData = new ArrayList<>();

    private Random mRandom = new Random();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recycler);

        mRvWrapper = findViewById(R.id.rv_wrapper);
        mTvCacheLog = findViewById(R.id.tv_cache_log);
        mTvBindLog = findViewById(R.id.tv_bind_log);
        mSvBindLog = findViewById(R.id.sv_bind_log);

        logString = new StringBuilder();

        mAdapter = new RecyclerAdapter(this, mData);
        mAdapter.setBindListener(this);

        mRvWrapper.setLayoutManager(new LinearLayoutManager(this));
        mRvWrapper.setAdapter(mAdapter);

        mRvWrapper.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                showCacheInfo();
            }
        });

        mRvWrapper.setLayoutListener(new RecycleViewWrapper.LayoutListener() {
            @Override
            public void beforeLayout() {
                try {
                    Field recyclerField =
                            Class.forName("androidx.recyclerview.widget.RecyclerView")
                                    .getDeclaredField("mRecycler");
                    recyclerField.setAccessible(true);
                    RecyclerView.Recycler recyclerInstance =
                            (RecyclerView.Recycler) recyclerField.get(mRvWrapper);

                    Class<?> recyclerClass = Class.forName(recyclerField.getType().getName());
                    Field attachedScrapField = recyclerClass.getDeclaredField("mAttachedScrap");
                    attachedScrapField.setAccessible(true);
                    // 杩欓噷鐨?mAttachedScrap 灞炴€у€艰櫧鐒舵槸 final锛屼絾鍙互琚弽灏勪慨鏀?                    attachedScrapField.set(recyclerInstance,
                            new ArrayListWrapper<RecyclerView.ViewHolder>());
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void afterLayout() {
                showCacheInfo();
            }
        });

        createData(40, 1);

    }

    @Override
    public void onBind(final String text) {
        mTvBindLog.post(new Runnable() {
            @Override
            public void run() {
                mTvBindLog.setText(mTvBindLog.getText() + text);
                mSvBindLog.fullScroll(View.FOCUS_DOWN);
            }
        });
    }

    /**
     * 浜х敓鏁版嵁
     *
     * @param count     鏁版嵁閲?     * @param typeCount 鏁版嵁绉嶇被
     */
    private void createData(int count, int typeCount) {

        mData.clear();
        for (int i = 0; i < count; ++i) {
//            int type = mRandom.nextInt(typeCount);
            int type = i >= 20 ? RecyclerBean.TYPE_FOUR : RecyclerBean.TYPE_ONE;
            switch (type) {
                case RecyclerBean.TYPE_ONE:
                    mData.add(new RecyclerBean<>(type, "A" + i));
                    break;
                case RecyclerBean.TYPE_TWO:
                    break;
                case RecyclerBean.TYPE_THREE:
                    break;
                case RecyclerBean.TYPE_FOUR:
                    mData.add(new RecyclerBean<>(type, "D" + i));
                    break;
            }
        }
        mAdapter.notifyDataSetChanged();

    }

    /**
     * 鏄剧ず缂撳瓨鏁版嵁
     */
    private void showCacheInfo() {
        try {
            // 鑾峰彇 Recycler
            Field recyclerField = Class.forName("androidx.recyclerview.widget.RecyclerView")
                    .getDeclaredField("mRecycler");
            recyclerField.setAccessible(true);
            RecyclerView.Recycler recycler = (RecyclerView.Recycler) recyclerField.get(mRvWrapper);

            Class<? extends RecyclerView.Recycler> recyclerClass = recycler.getClass();
            // 鑾峰彇 mAttachScrap 灞炴€?            Field attachScrapFiled = recyclerClass.getDeclaredField("mAttachedScrap");
            // 鑾峰彇 mChangedScrap 灞炴€?            Field changeScrapField = recyclerClass.getDeclaredField("mChangedScrap");
            // 鑾峰彇 mCachedViews 灞炴€?            Field cacheViewField = recyclerClass.getDeclaredField("mCachedViews");
            // 鑾峰彇 mRecyclerPool 灞炴€?            Field recyclePoolField = recyclerClass.getDeclaredField("mRecyclerPool");
            // 鑾峰彇 mViewCacheMax 灞炴€?            Field viewCacheMaxField = recyclerClass.getDeclaredField("mViewCacheMax");

            // 璁剧疆涓哄彲浠ヨ闂?            attachScrapFiled.setAccessible(true);
            changeScrapField.setAccessible(true);
            cacheViewField.setAccessible(true);
            recyclePoolField.setAccessible(true);
            viewCacheMaxField.setAccessible(true);

            // 鑾峰彇 mAttachScrap 灞炴€у€?            ArrayListWrapper<RecyclerView.ViewHolder> attachedScrap =
                    (ArrayListWrapper<RecyclerView.ViewHolder>) attachScrapFiled.get(recycler);
            // 鑾峰彇 mChangedScrap 灞炴€у€?            ArrayList<RecyclerView.ViewHolder> changeScrap =
                    (ArrayList<RecyclerView.ViewHolder>) changeScrapField.get(recycler);
            // 鑾峰彇 mCachedViews 灞炴€у€?            ArrayList<RecyclerView.ViewHolder> cachedView =
                    (ArrayList<RecyclerView.ViewHolder>) cacheViewField.get(recycler);
            // 鑾峰彇 mRecyclerPool 灞炴€у€?            RecyclerView.RecycledViewPool recycledViewPool =
                    (RecyclerView.RecycledViewPool) recyclePoolField.get(recycler);

            // 鑾峰彇 mViewCacheMax 灞炴€у€?            int viewCacheSize = (int) viewCacheMaxField.get(recycler);

            // 鑾峰彇 RecycleViewPool 涓殑 mScrap 灞炴€?            Field scrapField = recycledViewPool.getClass().getDeclaredField("mScrap");
            scrapField.setAccessible(true);
            SparseArray poolScrap = (SparseArray) scrapField.get(recycledViewPool);

            String info = "mAttachedScrap锛堜竴缂擄級size is:" + attachedScrap.maxSize + "\n"
                    + "================\n"
                    + "mCachedViews锛堜簩缂擄級 max size is:" + viewCacheSize + "\n"
                    + "================\n"
                    + getViewHolderArrInfo("mChangedScrap", changeScrap)
                    + getCacheViewTargetPositionInfo(mRvWrapper,
                    (LinearLayoutManager) mRvWrapper.getLayoutManager())
                    + getViewHolderArrInfo("mCachedViews(浜岀紦)", cachedView)
                    + getRVPoolInfo(poolScrap);

            mTvCacheLog.setText(info);
            Log.i(TAG, "log\n" + info);
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (NoSuchFieldException e) {
            e.printStackTrace();
        }
    }

    private String getViewHolderArrInfo(String typeName,
                                        ArrayList<RecyclerView.ViewHolder> viewHolders) {
        if (viewHolders != null) {

            StringBuilder result = new StringBuilder();

            result.append("[")
                    .append(typeName)
                    .append("]")
                    .append(" size is: ")
                    .append(viewHolders.size())
                    .append("\n");

            result.append("[")
                    .append(typeName)
                    .append("]")
                    .append("info: \n");

            if (viewHolders.size() == 0) {
                result.append("NO ViewHolders.\n");
                result.append("================\n");
                return result.toString();
            }

            for (int i = 0; i < viewHolders.size(); i++) {
                RecyclerView.ViewHolder item = viewHolders.get(i);
                result.append(typeName)
                        .append("[")
                        .append(i)
                        .append("]")
                        .append(item.toString())
                        .append("\n");
            }

            result.append("================\n");
            return result.toString();

        }
        return "";
    }

    private String getRVPoolInfo(SparseArray poolScrap) {
        StringBuilder result = new StringBuilder();

        result.append("mRecyclerPool(鍥涚紦) info: \n");
        for (int i = 0; i < poolScrap.size(); i++) {
            Object scrapDataItem = poolScrap.get(i);
            if(scrapDataItem == null){
                continue;
            }
            try {
                // 鑾峰彇璇?scrapData 鐨?mScrapHeap 鍜?mMaxScrap 灞炴€?                Class<?> scrapDataClass = Class
                        .forName("androidx.recyclerview.widget.RecyclerView$RecycledViewPool$ScrapData");
                Field scrapHeapField = scrapDataClass.getDeclaredField("mScrapHeap");
                Field maxScrapField = scrapDataClass.getDeclaredField("mMaxScrap");
                scrapHeapField.setAccessible(true);
                maxScrapField.setAccessible(true);

                ArrayList<RecyclerView.ViewHolder> item =
                        (ArrayList<RecyclerView.ViewHolder>) scrapHeapField.get(scrapDataItem);

                for (int j = 0; j < item.size(); j++) {
                    if (j == 0) {
                        result.append("----------绗?)
                                .append(j)
                                .append("涓?----------\n");
                        result.append("mScrap[")
                                .append(i)
                                .append("] max size is:")
                                .append(maxScrapField.get(poolScrap.get(i)))
                                .append("\n");
                    } else if (j == item.size() - 1) {
                        result.append("-----------------------------\n");
                    }

                    result.append("mScrap[")
                            .append(i)
                            .append("] 涓殑 mScrapHeap[")
                            .append(j)
                            .append("] info is:")
                            .append(item.get(j))
                            .append("\n");
                }
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            } catch (NoSuchFieldException e) {
                e.printStackTrace();
            }
        }

        result.append("================\n");

        return result.toString();
    }

    private String getCacheViewTargetPositionInfo(RecyclerView recycledView,
                                                  LinearLayoutManager layoutManager) {
        int firstItemPosition = layoutManager.findFirstVisibleItemPosition();
        int lastItemPosition = layoutManager.findLastVisibleItemPosition();

        try {
            Field mLayoutStateField = layoutManager.getClass().getDeclaredField("mLayoutState");
            mLayoutStateField.setAccessible(true);
            Class<?> layoutStateClass =
                    Class.forName("androidx.recyclerview.widget.LinearLayoutManager$LayoutState");
            Field mCurrentPositionField = layoutStateClass.getDeclaredField("mCurrentPosition");
            mCurrentPositionField.setAccessible(true);

            return "target mCachedView position:" +
                    mCurrentPositionField.get(mLayoutStateField.get(recycledView.getLayoutManager()))
                    + "\n"
                    + "firstItemPosition: " + firstItemPosition + "\n"
                    + "lastItemPosition: " + lastItemPosition + "\n"
                    + "================\n";
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "";
    }
}

