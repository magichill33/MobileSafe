package com.ly.mobilesafe;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.provider.ContactsContract.Contacts;
import android.app.Activity;
import android.content.ContentResolver;
import android.content.Intent;
import android.database.Cursor;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.SimpleAdapter;

public class SelectContactActivity extends Activity {

    private ListView listSelectContact;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_contact);
        listSelectContact = (ListView) findViewById(R.id.list_select_contact);
        final List<Map<String, String>> data = getContactInfo();
        listSelectContact.setAdapter(new SimpleAdapter(this,
                data,R.layout.contact_item_view,
                new String[]{"name","phone"}, new int[]{R.id.tv_name,R.id.tv_phone}));
        listSelectContact.setOnItemClickListener(new OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
                String phone = data.get(position).get("phone");
                Intent intent = new Intent();
                intent.putExtra("phone", phone);
                setResult(0,intent);
                finish();
            }
        });
    }

    private List<Map<String, String>> getContactInfo()
    {
        //存储所有的联系人信息
        List<Map<String, String>> list = new ArrayList<Map<String,String>>();
        //得到一个内容解析器
        ContentResolver resolver = getContentResolver();
        Uri uri = Uri.parse("content://com.android.contacts/raw_contacts");
        Uri uriData = Uri.parse("content://com.android.contacts/data");
        Cursor cursor = resolver.query(uriData, new String[]{"contact_id"},
                null, null, null);
        while(cursor.moveToNext())
        {
            String contact_id = cursor.getString(0);
            if(contact_id!=null)
            {
                //用来存放具体的一个联系人
                Map<String, String> map = new HashMap<String, String>();
                Cursor dataCursor = resolver.query(uriData, new String[]{"data1","mimetype"},
                        "contact_id=?", new String[]{contact_id}, null);
                while(dataCursor.moveToNext())
                {
                    String data1 = dataCursor.getString(0);
                    String mimetype = dataCursor.getString(1);
                    System.out.println("data1=="+data1+"==mimetype=="+mimetype);
                    if("vnd.android.cursor.item/name".equals(mimetype))
                    {
                        map.put("name", data1);
                    }else if("vnd.android.cursor.item/phone_v2".equals(mimetype))
                    {
                        map.put("phone", data1);
                    }
                }
                list.add(map);
                dataCursor.close();
            }
        }
        cursor.close();
        return list;
    }
}
