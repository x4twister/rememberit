/*
 * Copyright (c) 2020 x4twister
 * This code is licensed under MIT license (see LICENSE.txt for details)
 */

package ru.x4twister.rememberit.game

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import ru.x4twister.rememberit.R
import ru.x4twister.rememberit.TopicLab
import ru.x4twister.rememberit.databinding.FragmentGameBinding
import ru.x4twister.rememberit.databinding.ListItemAnswerBinding

class GameFragment: Fragment() {

    private var topicId:String=""

    private val gameRound by lazy {
        GameRound(TopicLab.getTopic(topicId)!!)
    }

    private val gameViewModel by lazy {
        GameViewModel(gameRound)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        topicId=arguments!!.getSerializable(ARG_TOPIC_ID) as String
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val binding:FragmentGameBinding=DataBindingUtil
            .inflate(inflater,R.layout.fragment_game,container,false)

        binding.viewModel=gameViewModel

        binding.recycleView.run {
            layoutManager= LinearLayoutManager(activity)
            adapter=AnswerAdapter(gameRound.answers)
        }

        return binding.root
    }

    companion object {

        const val ARG_TOPIC_ID="topic_id"

        fun newInstance(topicId:String): GameFragment {
            val args=Bundle()
            args.putSerializable(ARG_TOPIC_ID,topicId)

            val fragment=GameFragment()
            fragment.arguments=args
            return fragment
        }
    }

    inner class AnswerHolder(private val binding: ListItemAnswerBinding) : RecyclerView.ViewHolder(binding.root){

        fun bind(Answer: String) {
            binding.viewModel!!.answer=Answer
        }

        init {
            binding.viewModel=AnswerViewModel(gameRound,object: AnswerViewModel.Callback {
                override fun answerChecked() {
                    gameViewModel.notifyChange()
                }
            })
        }
    }

    inner class AnswerAdapter(private val answers: List<String>): RecyclerView.Adapter<AnswerHolder>() {

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AnswerHolder {
            val inflater=LayoutInflater.from(activity)
            val binding: ListItemAnswerBinding =DataBindingUtil
                .inflate(inflater,R.layout.list_item_answer,parent,false)

            return AnswerHolder(binding)
        }

        override fun getItemCount()=answers.size

        override fun onBindViewHolder(holder: AnswerHolder, position: Int) {
            holder.bind(answers[position])
        }
    }
}