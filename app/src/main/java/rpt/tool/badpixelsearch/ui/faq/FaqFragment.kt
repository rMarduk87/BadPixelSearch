package rpt.tool.badpixelsearch.ui.faq

import android.annotation.SuppressLint
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

    private var lstFaq: MutableList<FAQModel> = arrayListOf()
    private var answerBlockLst: MutableList<LinearLayout> = arrayListOf()
    private var imgFaqLst: MutableList<ImageView> = arrayListOf()

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
        lstFaq.add(faqModel)

        faqModel = FAQModel()
        faqModel.question = (requireContext().resources.getString(R.string.faq_question_2))
        faqModel.answer = (requireContext().resources.getString(R.string.faq_answer_2))
        lstFaq.add(faqModel)

        faqModel = FAQModel()
        faqModel.question = (requireContext().resources.getString(R.string.faq_question_3))
        faqModel.answer = (requireContext().resources.getString(R.string.faq_answer_3))
        lstFaq.add(faqModel)

        faqModel = FAQModel()
        faqModel.question = (requireContext().resources.getString(R.string.faq_question_4))
        faqModel.answer = (requireContext().resources.getString(R.string.faq_answer_4))
        lstFaq.add(faqModel)

        faqModel = FAQModel()
        faqModel.question = (requireContext().resources.getString(R.string.faq_question_5))
        faqModel.answer = (requireContext().resources.getString(R.string.faq_answer_5))
        lstFaq.add(faqModel)


    }

    @SuppressLint("InflateParams")
    private fun loadFAQData() {
        binding.faqBlock.removeAllViews()
        for (k in lstFaq.indices) {
            val rowModel = lstFaq[k]
            val layoutInflater = LayoutInflater.from(requireContext())
            val itemView = layoutInflater.inflate(R.layout.row_item_faq, null, false)
            val lblQuestion = itemView.findViewById<AppCompatTextView>(R.id.lbl_question)
            val lblAnswer = itemView.findViewById<AppCompatTextView>(R.id.lbl_answer)
            val questionBlock = itemView.findViewById<LinearLayout>(R.id.question_block)
            val answerBlock = itemView.findViewById<LinearLayout>(R.id.answer_block)
            val imgFaq = itemView.findViewById<ImageView>(R.id.img_faq)
            answerBlockLst.add(answerBlock)
            imgFaqLst.add(imgFaq)
            lblQuestion.text = rowModel.question
            lblAnswer.text = rowModel.answer
            questionBlock.setOnClickListener {
                if (answerBlock.visibility == View.GONE) {
                    viewAnswer(k)
                    imgFaq.setImageResource(R.drawable.ic_faq_minus)
                    AnimationUtils.expand(answerBlock)
                } else {
                    imgFaq.setImageResource(R.drawable.ic_faq_plus)
                    AnimationUtils.collapse(answerBlock)
                }
            }
            binding.faqBlock.addView(itemView)
        }
    }

    private fun viewAnswer(pos: Int) {
        for (k in answerBlockLst.indices) {
            if (k == pos) continue else {
                imgFaqLst[k].setImageResource(R.drawable.ic_faq_plus)
                AnimationUtils.collapse(answerBlockLst[k])
            }
        }
    }
}