import unittest as ut


class MyTestBase(ut.TestCase):
    def test_a(self):
        self.assertEqual(0, 0)

    def test_c(self):
        self.assertEqual(0, 0)


class DerivedTest(MyTestBase):
    def test_w4eolcefvnkm3(self):
        self.assertEqual(0, 0)

    def not_test_ccc(self):
        self.assertEqual(0, 0)

    def test_acb(self):
        pass

    def test_xyz(self):
        self.assertEqual(0, 0)
        self.assertEqual(1, 2)
        self.assertEqual(3, 4)
