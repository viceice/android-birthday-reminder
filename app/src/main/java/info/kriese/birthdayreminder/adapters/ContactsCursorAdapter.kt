package info.kriese.birthdayreminder.adapters

import android.database.Cursor
import android.net.Uri
import android.provider.ContactsContract
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.core.database.getStringOrNull
import androidx.recyclerview.widget.RecyclerView
import info.kriese.birthdayreminder.R


class ContactsCursorAdapter :
    BaseCursorAdapter<ContactsCursorAdapter.FriendViewHolder?>(null) {
    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): FriendViewHolder {
        val formNameView: View = LayoutInflater.from(parent.context)
            .inflate(R.layout.contacts_list_item, parent, false)
        return FriendViewHolder(formNameView)
    }

    override fun onBindViewHolder(
        holder: FriendViewHolder?,
        cursor: Cursor?
    ) {
        if (holder == null ||cursor == null) return

        var mColumnIndexName = cursor.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME_PRIMARY)
        val contactName = cursor.getString(mColumnIndexName)
        holder.nameTextView.text = contactName

        mColumnIndexName  = cursor.getColumnIndex(ContactsContract.Contacts.PHOTO_THUMBNAIL_URI)
        val photoUri = cursor.getStringOrNull(mColumnIndexName);

        if (photoUri != null)
            holder.imageView.setImageURI(Uri.parse(photoUri))
        else
            holder.imageView.setImageDrawable(ContextCompat.getDrawable(holder.imageView.context,R.drawable.ic_launcher_foreground))

        mColumnIndexName  = cursor.getColumnIndex(ContactsContract.CommonDataKinds.Event.START_DATE)
        var bDate = cursor.getStringOrNull(mColumnIndexName)

        if (bDate != null) {
            val parts = bDate.split("-");
            bDate = when {
                parts.count() == 3 -> "${parts[2]}.${parts[1]}.${parts[0]}"
                parts.count() == 4 -> "${parts[3]}.${parts[2]}."
                else -> ""
            };

            holder.textDate.text = bDate
        }
    }

    inner class FriendViewHolder(itemView: View) :
        RecyclerView.ViewHolder(itemView) {
        val nameTextView = itemView.findViewById<TextView>(R.id.textViewName)
        val imageView = itemView.findViewById<ImageView>(R.id.imageView)
        val textDate = itemView.findViewById<TextView>(R.id.textDate)

    }
}