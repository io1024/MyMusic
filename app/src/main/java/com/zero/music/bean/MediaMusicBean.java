package com.zero.music.bean;

import android.database.Cursor;
import android.os.Parcel;
import android.os.Parcelable;
import android.provider.MediaStore.Audio.Media;

/**
 * 视频实体
 */
public class MediaMusicBean implements Parcelable {

    private String name;
    private String path;
    private int duration;
    private long fileSize;
    //private String artist;//作者

    public MediaMusicBean() {
    }

    /**
     * @param cursor 游标
     * @return 获取：通过 Cursor 查询 媒体文件的实体
     */
    public static MediaMusicBean getMediaMusicBean(Cursor cursor) {
        MediaMusicBean bean = new MediaMusicBean();
        bean.name = cursor.getString(cursor.getColumnIndex(Media.TITLE));//名字
        bean.path = cursor.getString(cursor.getColumnIndex(Media.DATA));//路径
        bean.duration = cursor.getInt(cursor.getColumnIndex(Media.DURATION));//时长
        bean.fileSize = cursor.getLong(cursor.getColumnIndex(Media.SIZE));//大小
        return bean;
    }

    protected MediaMusicBean(Parcel in) {
        name = in.readString();
        path = in.readString();
        duration = in.readInt();
        fileSize = in.readLong();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(name);
        dest.writeString(path);
        dest.writeInt(duration);
        dest.writeLong(fileSize);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<MediaMusicBean> CREATOR = new Creator<MediaMusicBean>() {
        @Override
        public MediaMusicBean createFromParcel(Parcel in) {
            return new MediaMusicBean(in);
        }

        @Override
        public MediaMusicBean[] newArray(int size) {
            return new MediaMusicBean[size];
        }
    };

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public int getDuration() {
        return duration;
    }

    public void setDuration(int duration) {
        this.duration = duration;
    }

    public long getFileSize() {
        return fileSize;
    }

    public void setFileSize(long fileSize) {
        this.fileSize = fileSize;
    }
}
