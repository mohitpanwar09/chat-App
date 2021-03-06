package com.example.letstalk.messages


import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.recyclerview.widget.DividerItemDecoration
import com.example.letstalk.R
import com.example.letstalk.modelUser.User
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.squareup.picasso.Picasso
import com.xwray.groupie.*
import kotlinx.android.synthetic.main.activity_new_message.*
import kotlinx.android.synthetic.main.user_list_new_message.view.*

class NewMessage : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_new_message)

        supportActionBar?.title="select user"
        recycleView_newMessage.addItemDecoration(DividerItemDecoration(this,DividerItemDecoration.VERTICAL))
        fetchUser()
    }

    private fun fetchUser(){
        val ref=FirebaseDatabase.getInstance().getReference("/User")
        ref.addListenerForSingleValueEvent(object:ValueEventListener{
            override fun onCancelled(error: DatabaseError) {
            }
            override fun onDataChange(snapshot: DataSnapshot) {
                val adapter=GroupAdapter<GroupieViewHolder>()
                snapshot.children.forEach{
                    val user=it.getValue(User::class.java)
                    if(user!=null && user.uid!=FirebaseAuth.getInstance().uid){
                        adapter.add(UserItem(user))
                    }
                }
                adapter.setOnItemClickListener { item, view ->
                    val userItem =item as UserItem
                    val intent= Intent(view.context,MainChat::class.java)
                    intent.putExtra("User_Key",userItem.user)
                    startActivity(intent)
                    finish()
                }
                recycleView_newMessage.adapter=adapter

            }

        })
    }

}

class UserItem( val user:User): Item<GroupieViewHolder>(){

    override fun bind(viewHolder: GroupieViewHolder, position: Int) {
        viewHolder.itemView.username_newMessage.text=user.username
        Picasso.get().load(user.profileImageUrl).into(viewHolder.itemView.userImage_newMessage)
    }
    override fun getLayout(): Int {
        return R.layout.user_list_new_message
    }

}