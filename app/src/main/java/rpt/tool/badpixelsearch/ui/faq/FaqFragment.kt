package rpt.tool.badpixelsearch.ui.faq

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.widget.ImageView
import android.widget.LinearLayout
import androidx.appcompat.widget.AppCompatTextView
import rpt.tool.badpixelsearch.BaseFragment
import rpt.tool.badpixelsearch.MainActivity
import rpt.tool.badpixelsearch.R
import rpt.tool.badpixelsearch.databinding.FaqFragmentBinding
import rpt.tool.badpixelsearch.model.faq.FAQModel
import rpt.tool.badpixelsearch.utils.view.custom.animation.AnimationUtils

class FaqFragment : BaseFragment<FaqFragmentBinding>(FaqFragmentBinding::inflate) {

    var lst_faq: MutableList<FAQModel> = arrayListOf()
    var answer_block_lst: MutableList<LinearLayout> = arrayListOf()
    var img_faq_lst: MutableList<ImageView> = arrayListOf()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.leftIconBlock.setOnClickListener{ finish() }

        setFAQData()
        loadFAQData()
    }

    private fun finish() {
        startActivity(Intent(requireContext(), MainActivity::class.java))
    }

    private fun setFAQData() {
        var faqModel = FAQModel()
        faqModel.question = (requireContext().resources.getString(R.string.faq_question_1))
        faqModel.answer = (requireContext().resources.getString(R.string.faq_answer_1))
        lst_faq.add(faqModel)

        faqModel = FAQModel()
        faqModel.question = (requireContext().resources.getString(R.string.faq_question_2))
        faqModel.answer = (requireContext().resources.getString(R.string.faq_answer_2))
        lst_faq.add(faqModel)

        faqModel = FAQModel()
        faqModel.question = (requireContext().resources.getString(R.string.faq_question_3))
        faqModel.answer = (requireContext().resources.getString(R.string.faq_answer_3))
        lst_faq.add(faqModel)

        faqModel = FAQModel()
        faqModel.question = (requireContext().resources.getString(R.string.faq_question_4))
        faqModel.answer = (requireContext().resources.getString(R.string.faq_answer_4))
        lst_faq.add(faqModel)

        faqModel = FAQModel()
        faqModel.question = (requireContext().resources.getString(R.string.faq_question_5))
        faqModel.answer = (requireContext().resources.getString(R.string.faq_answer_5))
        lst_faq.add(faqModel)


    }

    private fun loadFAQData() {
        binding.faqBlock.removeAllViews()
        for (k in lst_faq.indices) {
            val rowModel = lst_faq[k]
            val layoutInflater = LayoutInflater.from(requireContext())
            val itemView = layoutInflater.inflate(R.layout.row_item_faq, null, false)
            val lbl_question = itemView.findViewById<AppCompatTextView>(R.id.lbl_question)
            val lbl_answer = itemView.findViewById<AppCompatTextView>(R.id.lbl_answer)
            val question_block = itemView.findViewById<LinearLayout>(R.id.question_block)
            val answer_block = itemView.findViewById<LinearLayout>(R.id.answer_block)
            val img_faq = itemView.findViewById<ImageView>(R.id.img_faq)
            answer_block_lst.add(answer_block)
            img_faq_lst.add(img_faq)
            lbl_question.text = rowModel.question
            lbl_answer.text = rowModel.answer
            question_block.setOnClickListener {
                if (answer_block.visibility == View.GONE) {
                    viewAnswer(k)
                    img_faq.setImageResource(R.drawable.ic_faq_minus)
                    AnimationUtils.expand(answer_block)
                } else {
                    img_faq.setImageResource(R.drawable.ic_faq_plus)
                    AnimationUtils.collapse(answer_block)
                }
            }
            binding.faqBlock.addView(itemView)
        }
    }

    private fun viewAnswer(pos: Int) {
        for (k in answer_block_lst.indices) {
            if (k == pos) continue else {
                img_faq_lst[k].setImageResource(R.drawable.ic_faq_plus)
                AnimationUtils.collapse(answer_block_lst[k])
            }
        }
    }
}