package com.example.inventory

import android.content.Context.INPUT_METHOD_SERVICE
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.example.inventory.data.InventoryApplication
import com.example.inventory.data.InventoryViewModel
import com.example.inventory.data.InventoryViewModelFactory
import com.example.inventory.data.Item
import com.example.inventory.databinding.FragmentAddItemBinding

class AddItemFragment : Fragment() {

    private val navigationArgs: ItemDetailFragmentArgs by navArgs()

    private val viewModel: InventoryViewModel by activityViewModels {
        InventoryViewModelFactory(
            (activity?.application as InventoryApplication).database
                .itemDao()
        )
    }

    lateinit var item: Item

    private var _binding: FragmentAddItemBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentAddItemBinding.inflate(inflater, container, false)
        return binding.root
    }

    //проверяем что поля заполнены
    private fun isEntryValid(): Boolean {
        return viewModel.isEntryValid(
            binding.itemName.text.toString(),
            binding.itemPrice.text.toString(),
            binding.itemCount.text.toString()
        )
    }

    private fun addNewItem() {
        if (isEntryValid()) {
            viewModel.addNewItem(
                binding.itemName.text.toString(),
                binding.itemPrice.text.toString(),
                binding.itemCount.text.toString(),
            )
        }

        val action = AddItemFragmentDirections.actionAddItemFragmentToItemListFragment()
        findNavController().navigate(action)
    }

    /* TODO
         создай метод  bind() принимающий Item
         этот метод будет назначать полям xml файла соответствующие
         значения, переданные при нажатии кнопки редактирования элемента
         так как мы переводим поля объекта в поля xml файла
         то все значения должны быть типа String
         - создай переменную price и присвой ей отформатированное значение
         поля itemPrice -> "%.2f".format(item.itemPrice)
         - через binding.apply назначь трем полям xml файла значения
         переданные при переходе на редактирование элемента
         используй setText(item.itemName,а втророй параметр
         TextView.BufferType.SPANNABLE), не забывай все приводить к строке
         что не является строкой */
    private fun bind(item: Item) {
        val price = "%.2f".format(item.itemPrice)
        binding.apply {
            itemName.setText(item.itemName, TextView.BufferType.SPANNABLE)
            itemPrice.setText(price, TextView.BufferType.SPANNABLE)
            itemCount.setText(item.quantityInStock.toString(), TextView.BufferType.SPANNABLE)
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        /* TODO
            создай переменную id принимающую значение из переданного
            аргумента из ItemDetailFragment
            используй navigationArgs. Имя аргумента находится в nav_graph.xml
        */
        val id = navigationArgs.itemId
        /* TODO
            реализуй выбор из двух вариантов: if()else{}
            если у нас переданный id > 0 то мы имеем дела с уже существующим
            элементом
            поэтому необходимо через viewModel вызвать метод извлечения
            объекта из базы по id, повесить на него обзервер, и полученный
            сохранить в lateinit var item: Item и
            объект связать через bind() и отобразить в xml файле
            - иначе в id лежит 0 а значит мы находимся в состоянии ДОБАВЛЕНИЯ
            нового элемента, и необходимо после заполнения полей
            чтоы при нажатии на кнопку SAVE отработал метод addNewItem()
        */
        if (id > 0) {
            viewModel.retrieveItem(id).observe(this.viewLifecycleOwner) {
                    selectedItem ->
                item = selectedItem
                bind(item)
            }
        } else {
            binding.saveAction.setOnClickListener {
                addNewItem()
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        // Hide keyboard.
        val inputMethodManager = requireActivity().getSystemService(INPUT_METHOD_SERVICE) as
                InputMethodManager
        inputMethodManager.hideSoftInputFromWindow(requireActivity().currentFocus?.windowToken, 0)
        _binding = null
    }
}
