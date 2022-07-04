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
import com.example.inventory.data.*
import com.example.inventory.databinding.FragmentAddItemBinding

class AddItemFragment : Fragment() {

    private val navigationArgs: ItemDetailFragmentArgs by navArgs()

    /*
    TODO(8)
         Создай объект viewModel через InventoryViewModelFactory(
         (activity?.application as InventoryApplication).database.itemDao())
     */
    private val viewModel: InventoryViewModel by activityViewModels {
        InventoryViewModelFactory(
            (activity?.application as InventoryApplication).database
                .itemDao()
        )
    }
    /*
    TODO(9)
         Создай переменную lateinit var item: Item для использования
         в связывании и наполнении файла xml
     */
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
    /*
    TODO(11)
         Создай метод isEntryValid(): Boolean
         его задача через метод isEntryValid() лежащий во viewModel
         проверить что все поля xml файла заполнены(не пустые)
         используем binding и не забываем все приводить к строке
         так как этот метод во вьюмодел принимает только строки
    */
    private fun isEntryValid(): Boolean {
        return viewModel.isEntryValid(
            binding.itemName.text.toString(),
            binding.itemPrice.text.toString(),
            binding.itemCount.text.toString()
        )
    }
    /*
        TODO(12)
             Создай метод addNewItem()
             его задача через метод addNewItem() лежащий во viewModel
             добавить все значения объекта Item в базу
             - через if проверяем что все поля валидные и
             - используем binding и не забываем все приводить к строке
             так как этот метод во вьюмодел принимает только строки
             он сам потом приведет поля объекта в соответствующие типы
             - после добавления соверши навигацию через AddItemFragmentDirections
             к списку всех объектов
        */
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
        //val price = "%.2f".format(item.itemPrice)
        binding.apply {
            itemName.setText(item.itemName, TextView.BufferType.SPANNABLE)
            itemPrice.setText(item.itemPrice.toString(), TextView.BufferType.SPANNABLE)
            itemCount.setText(item.quantityInStock.toString(), TextView.BufferType.SPANNABLE)

            /*TODO
               назначем кнопке другой ClickListener раз мы оказались
               в режиме редактирования */
            saveAction.setOnClickListener { updateItem() }
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
            сохранение изменений редактирования данного элемента
            мы реализуем через updateItem() внутри bind() метода
            saveAction.setOnClickListener { updateItem() }
            раз сработал bind() метод, значит мы в режиме редактирования
            и можно кнопке SAVE назначить другой ClickListener
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
            /*
            TODO(13)
             повесь OnClickListener на кнопку сохранить
             и передай в него метод добавления объекта
            */
            binding.saveAction.setOnClickListener {
                addNewItem()
            }
        }
    }
    /* TODO
        создай метод updateItem()
        его задача обновить в базе отредактированные данные
        - если isEntryValid()
        - то через viewModel.updateItem() передай туда все четыре значения
        которые установлены для отредактированного элемента
        через this.binding.itemName.text.toString() (id не приводи к строке)
        - после через AddItemFragmentDirections вернись к списку элементов */
    private fun updateItem() {
        if (isEntryValid()) {
            viewModel.updateItem(
                this.navigationArgs.itemId,
                this.binding.itemName.text.toString(),
                this.binding.itemPrice.text.toString(),
                this.binding.itemCount.text.toString()
            )
            val action = AddItemFragmentDirections.actionAddItemFragmentToItemListFragment()
            findNavController().navigate(action)
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
